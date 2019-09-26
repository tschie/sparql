# sparql
Demonstrates a custom REST query language with Spring JPA CriteriaBuilder for querying spatial entities in PostGIS.

## Try it

1. Run `docker-compose up` in the project root to start the `postgis` and `sparql` containers.
2. Add a couple location streams to the database: 
    
   ```
   curl -X POST -h "Content-Type: application/json" -d '[{"name":"Stream A"}, {"name":"Stream B"}]' http://localhost:8080/location_streams
   ```
3. Add a location to the database (use GET /location_streams to choose a locationStreamId):
   
   ```
   curl -X POST -h "Content-Type: application/json" -d '[{"locationStreamId": "f28988a9-dbef-4d24-a39c-2527168caf2a
   ","beg": "2019-05-07 00:00:00","end": "2019-05-08 00:00:00","geometry": "POINT (30 10 15)"}]' http://localhost
   :8080/locations
   ```
4. Put the following in a browser to query for locations: 

   ```
   http://localhost:8080/locations?q=distance($geometry,POINT(5 0))>25
   ```
