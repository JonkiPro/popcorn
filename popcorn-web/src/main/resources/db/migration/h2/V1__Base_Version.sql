CREATE TABLE persistent_logins (username VARCHAR(64) NOT NULL, series VARCHAR(64) PRIMARY KEY, token VARCHAR(64) NOT NULL, last_used TIMESTAMP NOT NULL);
CREATE TABLE contributions (ID BIGINT NOT NULL, created TIMESTAMP NOT NULL, entity_version INTEGER NOT NULL, field VARCHAR(255) NOT NULL, status VARCHAR(255) NOT NULL, user_comment VARCHAR(255), verification_comment VARCHAR(255), verification_date TIMESTAMP, movie_id BIGINT NOT NULL, user_id BIGINT NOT NULL, verification_user BIGINT, PRIMARY KEY (ID));
CREATE TABLE messages (id  SERIAL NOT NULL, created TIMESTAMP NOT NULL, date_of_read TIMESTAMP, entity_version INTEGER NOT NULL, visible_for_recipient BOOLEAN NOT NULL, visible_for_sender BOOLEAN NOT NULL, subject VARCHAR(255) NOT NULL, text VARCHAR(4000) NOT NULL, unique_id VARCHAR(255) NOT NULL UNIQUE, recipient_id BIGINT NOT NULL, sender_id BIGINT NOT NULL, PRIMARY KEY (id));
CREATE TABLE movies_info (ID BIGINT NOT NULL, dtype VARCHAR(31), entity_version INTEGER NOT NULL, reported_for_delete BOOLEAN NOT NULL, reported_for_update BOOLEAN NOT NULL, status VARCHAR(255) NOT NULL, movie_id BIGINT NOT NULL, box_office DECIMAL(38), box_office_country VARCHAR(255), country VARCHAR(255), genre VARCHAR(255), language VARCHAR(255), title_attribute VARCHAR(255), title_country VARCHAR(255), title VARCHAR(255), outline VARCHAR(239), file_id_in_cloud VARCHAR(255), file_provider VARCHAR(255), release_date_country VARCHAR(255), release_date DATE, review VARCHAR(20000), review_spoiler BOOLEAN, review_title VARCHAR(200), site_official VARCHAR(255), site VARCHAR(255), summary VARCHAR(2000), synopsis VARCHAR(100000), PRIMARY KEY (ID));
CREATE TABLE movies_ratings (id  SERIAL NOT NULL, DATE TIMESTAMP NOT NULL, rate INTEGER NOT NULL, movie_id BIGINT NOT NULL, user_id BIGINT NOT NULL, PRIMARY KEY (id));
CREATE TABLE movies (ID BIGINT NOT NULL, budget VARCHAR(255), favorite_count INTEGER NOT NULL, rating FLOAT, status VARCHAR(255) NOT NULL, title VARCHAR(255) NOT NULL, type VARCHAR(255) NOT NULL, PRIMARY KEY (ID));
CREATE TABLE users (id  SERIAL NOT NULL, activation_token VARCHAR(255), avatar_id_in_cloud VARCHAR(255), avatar_provider VARCHAR(255), created TIMESTAMP NOT NULL, email VARCHAR(255) NOT NULL UNIQUE, email_change_token VARCHAR(255), enabled BOOLEAN NOT NULL, entity_version INTEGER NOT NULL, modified_date TIMESTAMP NOT NULL, new_email VARCHAR(255), password VARCHAR(255) NOT NULL, unique_id VARCHAR(255) NOT NULL UNIQUE, username VARCHAR(36) NOT NULL UNIQUE, PRIMARY KEY (id));
CREATE TABLE contributions_ids_to_add (contribution_id BIGINT NOT NULL, element_id_to_add BIGINT NOT NULL);
CREATE TABLE contributions_ids_to_delete (contribution_id BIGINT NOT NULL, element_id_to_delete BIGINT NOT NULL);
CREATE TABLE contributions_sources (contribution_id BIGINT NOT NULL, SOURCES VARCHAR(255) NOT NULL);
CREATE TABLE contributions_ids_to_update (contribution_id BIGINT NOT NULL, old_element_id BIGINT NOT NULL, new_element_id BIGINT NOT NULL);
CREATE TABLE users_authorities (user_id BIGINT NOT NULL, authority VARCHAR(255) NOT NULL);
CREATE TABLE users_movie_permissions (user_id BIGINT NOT NULL, permission VARCHAR(255) NOT NULL);
CREATE TABLE users_favorites_movies (user_id BIGINT NOT NULL, movie_id BIGINT NOT NULL, PRIMARY KEY (user_id, movie_id));
CREATE TABLE users_friends (user_id BIGINT NOT NULL, user_friend_id BIGINT NOT NULL, PRIMARY KEY (user_id, user_friend_id));
CREATE TABLE users_received_invitations (user_invited_id BIGINT NOT NULL, user_id BIGINT NOT NULL, invitation_order INTEGER NOT NULL, PRIMARY KEY (user_invited_id, user_id));
CREATE TABLE users_sent_invitations (user_id BIGINT NOT NULL, user_invited_id BIGINT NOT NULL, invitation_order INTEGER NOT NULL, PRIMARY KEY (user_id, user_invited_id));
ALTER TABLE contributions ADD CONSTRAINT FK_contributions_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE contributions ADD CONSTRAINT FK_contributions_verification_user FOREIGN KEY (verification_user) REFERENCES users (id);
ALTER TABLE contributions ADD CONSTRAINT FK_contributions_movie_id FOREIGN KEY (movie_id) REFERENCES movies (ID);
ALTER TABLE messages ADD CONSTRAINT FK_messages_recipient_id FOREIGN KEY (recipient_id) REFERENCES users (id);
ALTER TABLE messages ADD CONSTRAINT FK_messages_sender_id FOREIGN KEY (sender_id) REFERENCES users (id);
ALTER TABLE movies_info ADD CONSTRAINT FK_movies_info_movie_id FOREIGN KEY (movie_id) REFERENCES movies (ID);
ALTER TABLE movies_ratings ADD CONSTRAINT FK_movies_ratings_movie_id FOREIGN KEY (movie_id) REFERENCES movies (ID);
ALTER TABLE movies_ratings ADD CONSTRAINT FK_movies_ratings_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE contributions_ids_to_add ADD CONSTRAINT FK_contributions_ids_to_add_contribution_id FOREIGN KEY (contribution_id) REFERENCES contributions (ID);
ALTER TABLE contributions_ids_to_delete ADD CONSTRAINT FK_contributions_ids_to_delete_contribution_id FOREIGN KEY (contribution_id) REFERENCES contributions (ID);
ALTER TABLE contributions_sources ADD CONSTRAINT FK_contributions_sources_contribution_id FOREIGN KEY (contribution_id) REFERENCES contributions (ID);
ALTER TABLE contributions_ids_to_update ADD CONSTRAINT FK_contributions_ids_to_update_contribution_id FOREIGN KEY (contribution_id) REFERENCES contributions (ID);
ALTER TABLE users_authorities ADD CONSTRAINT FK_users_authorities_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_movie_permissions ADD CONSTRAINT FK_users_movie_permissions_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_favorites_movies ADD CONSTRAINT FK_users_favorites_movies_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_favorites_movies ADD CONSTRAINT FK_users_favorites_movies_movie_id FOREIGN KEY (movie_id) REFERENCES movies (ID);
ALTER TABLE users_friends ADD CONSTRAINT FK_users_friends_user_friend_id FOREIGN KEY (user_friend_id) REFERENCES users (id);
ALTER TABLE users_friends ADD CONSTRAINT FK_users_friends_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_received_invitations ADD CONSTRAINT FK_users_received_invitations_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_received_invitations ADD CONSTRAINT FK_users_received_invitations_user_invited_id FOREIGN KEY (user_invited_id) REFERENCES users (id);
ALTER TABLE users_sent_invitations ADD CONSTRAINT FK_users_sent_invitations_user_invited_id FOREIGN KEY (user_invited_id) REFERENCES users (id);
ALTER TABLE users_sent_invitations ADD CONSTRAINT FK_users_sent_invitations_user_id FOREIGN KEY (user_id) REFERENCES users (id);
CREATE SEQUENCE movieSequenceGenerator INCREMENT BY 50 START WITH 0;
CREATE SEQUENCE contributionSequenceGenerator INCREMENT BY 50 START WITH 0;
CREATE SEQUENCE movieInfoSequenceGenerator INCREMENT BY 50 START WITH 0;