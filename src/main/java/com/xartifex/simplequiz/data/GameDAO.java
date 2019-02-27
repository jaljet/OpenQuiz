package com.xartifex.simplequiz.data;

import com.xartifex.simplequiz.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;

@ApplicationScoped
public class GameDAO {

    @Inject
    private EntityManager em;

    public Player findById(Long id) {
        return em.find(Player.class, id);
    }

    public Player findByName(String name) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Player> criteria = cb.createQuery(Player.class);
            Root<Player> player = criteria.from(Player.class);
            criteria.select(player).where(cb.equal(player.get("name"), name));
            return em.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Question getQuestionForNewPlayer() {
        return randomEntity(em, Question.class);
    }

    //taken from https://stackoverflow.com/a/3343394
    public <T> T randomEntity(EntityManager em, Class<T> clazz) {
        Query countQuery = em.createQuery("select count(id) from " + clazz.getName());
        long count = (Long) countQuery.getSingleResult();
        if (count == 0) {
            throw new IllegalStateException("No questions found in database!");
        }
        Random random = new Random();
        int number = random.nextInt((int) count);

        Query selectQuery = em.createQuery("from " + clazz.getName());
        selectQuery.setFirstResult(number);
        selectQuery.setMaxResults(1);
        return (T) selectQuery.getSingleResult();
    }

    public static final String GET_NEXT_QUESTIONS_COUNT_QUERY = "SELECT count(q) FROM Question q WHERE q NOT IN " +
            "(SELECT p.question from PlayerAnswerState p where p.player = :player)";
    public static final String GET_NEXT_QUESTION_QUERY = "SELECT q FROM Question q WHERE q NOT IN " +
            "(SELECT p.question from PlayerAnswerState p where p.player = :player)";

    public Question getNextQuestion(Player player) {
        Query countQuery = em.createQuery(GET_NEXT_QUESTIONS_COUNT_QUERY)
                .setParameter("player", player);
        long count = (Long) countQuery.getSingleResult();
        if (count == 0) {
            return null;
        }
        Random random = new Random();
        int number = random.nextInt((int) count);
        TypedQuery<Question> selectQuery = em.createQuery(GET_NEXT_QUESTION_QUERY,
                Question.class).setParameter("player", player);
        selectQuery.setFirstResult(number);
        selectQuery.setMaxResults(1);
        return selectQuery.getSingleResult();
    }

    public PlayerAnswerState updateAnswerState(PlayerAnswerState playerAnswerState) {
        return em.merge(playerAnswerState);
    }

    public void addPlayersAnswer(PlayerAnswerState playerAnswerState) {
        em.persist(playerAnswerState);
    }

    public void persistAnswer(PlayerAnswerState playerAnswer) {
        em.persist(playerAnswer);
    }

    public void addPlayer(Player player) {
        em.persist(player);
    }

    public Player updatePlayer(Player player) {
        return em.merge(player);
    }

    //
    public void addQuestion(Question question) {
        em.persist(question);
    }

    public void addQuestions(Collection<Question> questions) {
        for (Question question : questions) {
            addQuestion(question);
        }
    }
}
