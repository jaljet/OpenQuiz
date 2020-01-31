--delete all game data
delete from playeranswerstate;
delete from player_playeranswerstate;
delete from player;
delete from question;
delete from users_roles where user_id != 300991;
delete from users where email != 'admin@email.com';

--show all users with their results, even those who didn't play at all
select rownum(), email, office, total from (
select email, office, sum(points) total  from (
select email, office,  (case iscorrect when 'true' then 1 else 0 end) points
    from playeranswerstate a
    right join player p on a.player = p.id and iscorrect='true'
    right join users u on u.email = p.name)
group by email order by total desc)

--show players results
SELECT USERS.USERNAME, USERS.RUSNAME, USERS.EMAIL, WINNERCOUNT, WINNERTIME FROM
	USERS JOIN PLAYER ON USERS.EMAIL = PLAYER.NAME
	JOIN (SELECT WINNER, WINNERCOUNT, WINNERTIME FROM
		(SELECT PLAYER AS WINNER   , COUNT (*)  AS WINNERCOUNT  FROM
			PLAYERANSWERSTATE WHERE ISCORRECT = 'TRUE' OR CHECKEDISCORRECT = 1 GROUP BY PLAYER)
		JOIN  (SELECT PLAYER AS WINNERT, (MAX(EXPIRESON) - MIN (EXPIRESON))/1000 AS WINNERTIME FROM
			PLAYERANSWERSTATE GROUP BY PLAYER)
		ON WINNER = WINNERT)
	ON PLAYER.ID =  WINNER
ORDER BY WINNERCOUNT DESC, WINNERTIME