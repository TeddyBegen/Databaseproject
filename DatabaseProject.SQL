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

CREATE TABLE Article (
    ArticleId SERIAL PRIMARY KEY,
    UserID int,
    Title VARCHAR(20),
    ArticleType varchar(12),
    ArticleText text,
    Keywords varchar(50),
    ReviewerId_1 int,
    ReviewerId_2 int,
    ReviewerStatus_1 varchar(12),
    ReviewerStatus_2 varchar(12),
    ArticleStatus varchar(12),
    SubmissionDate date,
    ConferenceID int,
    FOREIGN KEY (UserID) REFERENCES Author(UserID),
    FOREIGN KEY (ReviewerId_1) REFERENCES Reviewer(UserID),
    FOREIGN KEY (ReviewerId_2) REFERENCES Reviewer(UserID),
    FOREIGN KEY (ConferenceID) REFERENCES SubmissionPeriod(ConferenceID)
);

CREATE TABLE Comment (
    CommentId SERIAL PRIMARY KEY,
    ReviewerId int,
    ArticleId int,
    CommentDate Date,
    CommentText varchar[5000],
    FOREIGN KEY (ReviewerId) REFERENCES Reviewer(UserID),
    FOREIGN KEY (ArticleId) REFERENCES Article(ArticleId)
);
