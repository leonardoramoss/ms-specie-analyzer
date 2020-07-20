# ms-species

# Reference Documentation

### Dependencies

    JDK 11
    Maven
    PostgresSQL
    Docker
    IntelliJ (Recommended)
    
### API
##### Endpoints

| Endpoint      | HTTP Method   | Description                                        | 
| ------------- | ------------- | -------------------------------------------------  |
| `/v1/simian`  | POST          | This endpoint receives a DNA sequence through an HTTP POST with a JSON payload that contains the following format indicated below. If the DNA is identified as a simian, an HTTP 200 - OK will be returned, otherwise a HTTP 403 - FORBIDDEN |
| `/v1/stats`   | GET           | This endpoint returns a JSON stats ratio defined by: amount of simian DNA’s, amount of human DNA’s, and the proportion of simians to the human population                      |
 
##### Endpoint - /v1/simian

- Simian requests payload examples

    ```json
    {
        "dna": ["ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"]
    }
    ```

    ```json
    {
        "dna": ["CTTAGA", "CTATGC", "CGTCTT", "ACACGT", "CCTCTA", "TCACTG"]
    }
    ```

    ```json
    {
        "dna": ["CTTAGA", "CTATGC", "CATCTT", "ACACGT", "CCTGTA", "TCACTG"]
    }
    ```

    ```json
    {
        "dna": ["CTTAGA", "CTATGC", "CTTCTT", "ACACGT", "CCTGTA", "TCACTG"]
    }
    ```

- Human request payload examples

    ```json
    {
        "dna": ["ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"]
    }
    ```
  
##### Endpoint - /v1/stats

- Response stats payload example

    ```json
    {
        "count_mutant_dna": 40,
        "count_human_dna": 100,
        "ratio": 0.4
    }
    ```
  Ratio stat between human and simian DNA

### Prod

The API is running at: https://species-ml.herokuapp.com

### Local

####Docker:

Run docker-compose on base project folder

- Build  a local image of application
    ```
    docker-compose build
    ```
- Running dependencies and application
    ```
    docker-compose up -d
    ```
    The application will start at http://localhost:8080/

####IDE:

- Running only dependencies and start application by IntelliJ
    ```
    docker-compose -f docker-compose-local.yml
    ```
    Make sure the environment file .env.local is configured in the IDE.
    See: https://plugins.jetbrains.com/plugin/7861-envfile
    
    The application will start at http://localhost:8080/
 
