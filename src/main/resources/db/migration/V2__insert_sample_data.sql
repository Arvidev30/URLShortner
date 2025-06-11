INSERT INTO users(email, password, name, role)
            VALUES('aravinddr@outlook.com', 'test1234', 'Aravind', 'ROLE_ADMIN'),
                  ('arvidevacc@gmail.com', 'test1234', 'Arvi', 'ROLE_USER');

INSERT INTO short_urls(short_key, original_url, created_by, created_at, expires_at, is_private, click_count)
            VALUES('fDwErh', 'https://www.youtube.com/', 1, CURRENT_TIMESTAMP, NULL, FALSE, 0),
                  ('HTrdEW', 'https://www.google.co.in/', 2, CURRENT_TIMESTAMP, NULL, FALSE, 0);