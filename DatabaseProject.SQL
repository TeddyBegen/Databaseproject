CREATE TABLE UserAccount (
    UserId SERIAL PRIMARY KEY,
    Email VARCHAR(40) NOT NULL,
    UserPassword VARCHAR(20) NOT NULL,
    FullName VARCHAR(50) NOT NULL,
    PhoneNumber bigint NOT NULL
);

CREATE TABLE Reviewer (
    ReviewerId int PRIMARY KEY,
    ResearchArea VARCHAR(20) NOT NULL,
    FOREIGN KEY (ReviewerId) REFERENCES UserAccount(UserId)
);

CREATE TABLE Author (
    AuthorId int PRIMARY KEY, 
    Affiliation VARCHAR(20) NOT NULL,
    FOREIGN KEY (AuthorId) REFERENCES UserAccount(UserId)
);

CREATE TABLE SubmissionPeriod (
    ConferenceID int PRIMARY KEY,
    StartDate date,
    EndDate date
);

CREATE TABLE Article (
    ArticleId int PRIMARY KEY,
    AuthorId int,
    Title VARCHAR(20),
    ArticleType varchar[12],
    ArticleText varchar[1000000000],
    Keywords varchar[50],
    ReviewerId_1 int,
    ReviewerId_2 int,
    ReviewerStatus_1 varchar[12],
    ReviewerStatus_2 varchar[12],
    SubmissionDate date,
    ConferenceID int,
    FOREIGN KEY (AuthorId) REFERENCES Author(AuthorId),
    FOREIGN KEY (ReviewerId_1) REFERENCES Reviewer(ReviewerId),
    FOREIGN KEY (ReviewerId_2) REFERENCES Reviewer(ReviewerId),
    FOREIGN KEY (ConferenceID) REFERENCES SubmissionPeriod(ConferenceID)
);

CREATE TABLE Comment (
    CommentId int PRIMARY KEY,
    ReviewerId int,
    ArticleId int,
    CommentDate Date,
    CommentText varchar[5000],
    FOREIGN KEY (ReviewerId) REFERENCES Reviewer(ReviewerId),
    FOREIGN KEY (ArticleId) REFERENCES Article(ArticleId)
);
