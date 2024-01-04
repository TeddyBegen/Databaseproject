CREATE TABLE UserAccount (
    UserId SERIAL PRIMARY KEY,
    Email VARCHAR(40),
    UserPassword VARCHAR(20),
    FullName VARCHAR(50),
    PhoneNumber bigint,
    AdminUser boolean NOT NULL DEFAULT false
);

CREATE TABLE Reviewer (
    UserId int PRIMARY KEY,
    ResearchArea VARCHAR(20),
    FOREIGN KEY (UserId) REFERENCES UserAccount(UserId)
);

CREATE TABLE Author (
    UserId int PRIMARY KEY, 
    Affiliation VARCHAR(20),
    FOREIGN KEY (UserId) REFERENCES UserAccount(UserId)
);

CREATE TABLE SubmissionPeriod (
    ConferenceID SERIAL PRIMARY KEY,
    StartDate date,
    EndDate date
);

CREATE TYPE article_status AS ENUM ('submitted', 'under review', 'rejected', 'accepted');

CREATE TYPE article_type AS ENUM ('short article', 'full article', 'poster');

CREATE TABLE Article (
    ArticleId SERIAL PRIMARY KEY,
    UserID int,
    Title VARCHAR(20),
    ArticleType article_type,
    ArticleText text,
    Keywords varchar(50),
    ArticleStatus article_status,
    SubmissionDate date,
    ConferenceID int,
    FOREIGN KEY (UserID) REFERENCES Author(UserID),
    FOREIGN KEY (ArticleReviewID_1) REFERENCES ArticleReview(ArticleReviewID),
    FOREIGN KEY (ArticleReviewID_1) REFERENCES ArticleReview(ArticleReviewID),
    FOREIGN KEY (ConferenceID) REFERENCES SubmissionPeriod(ConferenceID)
);

CREATE TABLE ArticleReview (
    UserID int,
    ArticleId int,
    Status article_status,
    CommentText text,
    PRIMARY KEY (UserID, ArticleId),
    FOREIGN KEY (UserID) REFERENCES Reviewer(UserID),
    FOREIGN KEY (ArticleId) REFERENCES Article(ArticleId)
);