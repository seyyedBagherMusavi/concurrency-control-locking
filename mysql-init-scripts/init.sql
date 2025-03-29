CREATE TABLE IF NOT EXISTS tbl_process (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           processed BOOLEAN NOT NULL DEFAULT FALSE
);

-- Insert 1000 sample tasks (only first time)
INSERT INTO tbl_process (processed)
SELECT false FROM mysql.help_relation, (SELECT @N:=0) dummy LIMIT 1000;
