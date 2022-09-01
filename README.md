# Holiday API Serivce
## 

A service will return the nearest holiday info by given date.

- Holiday.com as backend API provider
- SpringBoot as web framework


## How to run

Compile and build. Need to import holiday.com api's cert into Java truststore.

```sh
mvn compile build docker plugin
```

Once done, run the Docker image.

```sh
docker run -d -p 8000:8080 -d
```


Verify the result by opening browser or use Postman ( commandline similar )
```sh
curl 127.0.0.1:8000/holiday?country=US
```
