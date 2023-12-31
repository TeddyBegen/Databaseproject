CREATE TABLE UserAccount (
    UserId INT PRIMARY KEY,
    Username VARCHAR(20),
    UserPassword VARCHAR(20),
    FullName VARCHAR(50),
    PhoneNumber bigint
);

CREATE TABLE Reviewer (
    ReviewerId int PRIMARY KEY,
    ResearchArea VARCHAR(20),
    FOREIGN KEY (ReviewerId) REFERENCES UserAccount(UserId)
);

CREATE TABLE Author (
    AuthorId int PRIMARY KEY, 
    Affiliation VARCHAR(20),
    Email VARCHAR(40),
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