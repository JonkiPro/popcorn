package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.jonki.popcorn.core.PopcornCoreTestApplication;
import com.jonki.popcorn.core.jpa.repository.ContributionRepository;
import com.jonki.popcorn.core.jpa.repository.MessageRepository;
import com.jonki.popcorn.core.jpa.repository.MovieInfoRepository;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * Base class to save on configuration.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PopcornCoreTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestExecutionListeners(
        {
                DependencyInjectionTestExecutionListener.class,
                DirtiesContextTestExecutionListener.class,
                TransactionalTestExecutionListener.class,
                DbUnitTestExecutionListener.class
        }
)
@ActiveProfiles(
        {
                "db-h2",
                "mail"
        }
)
public abstract class DBIntegrationTestBase {

    @Autowired
    protected ContributionRepository contributionRepository;

    @Autowired
    protected MessageRepository messageRepository;

    @Autowired
    protected MovieInfoRepository movieInfoRepository;

    @Autowired
    protected MovieRepository movieRepository;

    @Autowired
    protected UserRepository userRepository;

    /**
     * Clean out the db after every test.
     */
    @After
    public void cleanup() {
        this.contributionRepository.deleteAll();
        this.messageRepository.deleteAll();
//        this.movieRepository.deleteAll();
        this.movieInfoRepository.deleteAll();
        this.userRepository.deleteAll();
    }
}
