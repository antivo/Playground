Task
====

Implement a word frequency analysis for Reddit comments


Specification
=============

Your application should expose two HTTP endpoints:


API Definition: /frequency/new
----------------------------------

__API Input:__ 

This API endpoint should take a Reddit comment URL as a parameter e.g.: 
  
    https://www.reddit.com/r/java/comments/32pj67/java_reference_in_gta_v_beautiful/.json

*Note: by adding .json to any Reddit comment URL you will get a json representation of this particular comment thread, the json representation should be used in order to do the word frequency analysis.* 
 
__API Response:__
 
For each request executed against the API endpoint you should return an unique identifier, which will be the input for the second API endpoint. 
 
__Workflow:__

* When the the API is being called, your code should do a HTTP request to fetch the json representation of the comment thread.
* Your code should then analyse the word frequency of all comments in this thread (the data for your analysis is stored in the ```body``` attribute), with following behavior: 
     
    * All whitespace characters should be ignored
    * All words should be converted to lower case
    * Following punctuation characters should be ignored: ```!,.?```   
  
    __Example:__
          
    ```The      quick \n brown fox \t jumps over the lazy dog.```  
    
    Your word frequency analysis should return following: 
    
    | Word        | Frequency  | 
    | ------------|------------|
    | the         | 2          |
    | quick       | 1          |
    | over        | 1          |
    | lazy        | 1          |
    | jumps       | 1          |
    | fox         | 1          |
    | dog         | 1          |
    | brown       | 1          |    

* The analysed data should be stored within a data store and referenced by an unique identifier (see API response).  


API Definition: /frequency/{id}?count=number_of_results
----------------------------------

__API Input:__ 

This API endpoint takes an ```id``` as input as well as a parameter ```count```  

__API Output:__

Returns the the top n elements (bounded by the ```count``` parameter) from the word frequency analysis
    
__Workflow:__
    
* When this API is being called, you will read the frequency analysis data stored in the database by using the given ```id``` parameter
* Return the top n results as a json object ordered by their frequency of occurrence

__Example: /frequency/ABC?count=2__

```
[
  {
    "word": "the",
    "count": 2
  },
  {
    "word": "fox",
    "count": 1
  }
]
```
    

Additional Information
======================

We have provided you with an example project, which should get you running quickly. Following features are immediately available when running the application: 
 
* Spring JPA
* H2 database running in memory (data will not be persistent across application restarts)

You are free to add / change any libraries which you might need to solve this exercise, the only requirement is that we do not have to setup / install any external software to run this application.    


Running the exercise with maven
===============================
 
    mvn spring-boot:run


Useful Resources
================

- How to install maven on your PC: [https://maven.apache.org/install.html](https://maven.apache.org/install.html)
- Reddit API reference: [https://www.reddit.com/dev/api](https://www.reddit.com/dev/api)
- Spring JPA reference: [http://docs.spring.io/spring-data/jpa/docs/1.8.2.RELEASE/reference/html/](http://docs.spring.io/spring-data/jpa/docs/1.8.2.RELEASE/reference/html/) 
