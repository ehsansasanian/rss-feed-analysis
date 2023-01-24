# RSS FEED ANALYSIS

This README describes an exercise to implement a hot topic analysis for RSS feeds using Spring JPA and an in-memory H2 database. The application should expose two HTTP endpoints:


### Prerequisites
- Java 17
- Maven

### Getting Started
1. Clone the repository:

```git clone https://github.com/ehsansasanian/rss-feed-analysis.git```

2. Change directory to the project:
```cd rss-feed-analysis```

   
3. Run the app
```mvn clean spring-boot:run```

### API:

POST: /analyse

Takes at least two RSS URLs as a parameter and returns an unique identifier for the analysis. The API should fetch the RSS feeds and analyze the entries to find potential hot topics, storing the results in the database with the given identifier.

Example CURL:

```
curl --location --request POST 'localhost:8080/analyse' --header 'Content-Type: application/json' --data-raw '{"urls":["http://rss.cnn.com/rss/edition.rss","https://news.google.com/rss?hl=en-US&gl=US&ceid=US:en", "https://search.cnbc.com/rs/search/combinedcms/view.xml?partnerId=wrss01&id=100003114"]}'
```

Returns a uuid

GET: /analyse/frequency/{uuid}

Takes an id as input and returns the top three most frequent hot topics from the analysis with the given id, including the original news header and link to the full text.

Example CURL:

```curl --location --request GET 'localhost:8080/analyse/frequency/20a597b7-66c4-4b16-8ea3-30d922c16930'```

### NOTE
Additional libraries may be added as needed, but no external software should need to be installed.

Thank you for using this software.