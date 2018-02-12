## [Stock Market](https://github.com/mailtoharshgupta/stocks/)
Stock Market is a web application that implements RESTful API for stock market resources such as stocks.
Goal of the app is to provide REST APIs for basic create, read and update operations on stocks repository.

## Architecture

![stock-market-architecture](https://user-images.githubusercontent.com/13452011/36092972-378a36cc-100f-11e8-85d1-8acdfccfd230.png)

- **Rest Controller** : This layer is responsible for exposing RESTful interface for all the API operations. As of now ths layer provides APIs for doing CRUD operations on stocks respository

- **Service Layer** : This layer is responsible for performing any business logic to the resoucres. A request received from REST Controller layer is transferred to the service layer. Service layer interacts with the Data Access Layer to fetch the required data, applies any business logic applicable to finally send it back to Controller layer.

- **Data Access Layer** : This layer acts as an interface for fetching the requried data for Service Layer. This layer acts as an umbrella for any data source used for persisting the resources. As of now, we have used H2 database for storing the data. In future if we plan to move to MySQL, MongoDB or use a distrbuted cache to fetch the data, only the Data Access Layer will have to make changes, where as all the other layers will remain intact.

## Dependencies

- **Spring Boot** : Version - 1.5.10 
- **JDK** : Version - 1.8
- **Swagger** : Version - 2.8.0
- **H2** : Version - 2.9.4
- **Thymeleaf** : Version - 1.5.10
- **Bootstrap** : Version - 3.3.7
- **Lombok** : Version - 1.16.18
- **Apache Maven** : Version - 3.3.3
- **JUnit** : Version - 4.12

## Features

### API 

- **GET(/api/stocks/)** : Used to get a **paginated** list of stocks. As number of records can grow, hence pagination was implemented.
- **GET(/api/stocks/{id})** : Used to get the stock identified by id.
- **POST(/api/stocks/)** : Used to create a stock.
- **PUT(/api/stocks/{id})** : Used to update the price of a stock identified by id.

### Web UI
A web UI can be opened to see the list of stocks. The link to dashboard is : http://localhost:8080/.
It displays the paginated list of stocks, with option to switch pages.

### Documentation
The in depth documentation of the APIs can be found on the swagger link(http://localhost:8080/swagger-ui.html) 

## Getting started

### Installing
To get the source, clone the git repository.
```sh
git clone https://github.com/mailtoharshgupta/stocks.git
```
### Building
`mvn clean install`
## Running the app 
Run the following command `java -jar StockMarketWeb/target/StockMarketWeb-0.0.1-SNAPSHOT.jar`

## API Usage Quickstart
### Getting a list of stocks:
```sh
Request
curl -v 'http://localhost:8080/api/stocks/?pageNumber=0&size=2'

Response : 
{
    "content": [
        {
            "id": 1,
            "name": "20MICRONS",
            "price": 318.0,
            "updated": 1518439339601
        },
        {
            "id": 2,
            "name": "21STCENMGM",
            "price": 344.0,
            "updated": 1518439339715
        }
    ],
    "first": true,
    "last": false,
    "number": 0,
    "numberOfElements": 2,
    "size": 2,
    "sort": null,
    "totalElements": 202,
    "totalPages": 101
}
```
### Get a stock identified by ID
```sh
Request
curl -v 'http://localhost:8080/api/stocks/1'

Response
{
    "id": 1,
    "name": "20MICRONS",
    "price": 318.0,
    "updated": 1518439339601
}

```
### Create a stock
```sh
Request
curl -v -HContent-Type:application/json -X POST --data-binary '{"name":"STOCK_NAME", "price":150.00}' http://localhost:8080/api/stocks/

Response
{
    "id": 203,
    "name": "STOCK_NAME",
    "price": 150.0,
    "updated": 1518440175700
}

```
### Update the price of a stock
```sh
Request
curl -v -HContent-Type:application/json -X PUT --data-binary '{"name":"STOCK_NAME", "price":100.00}' http://localhost:8080/api/stocks/203

Response
{
    "id": 203,
    "name": "STOCK_NAME",
    "price": 100.0,
    "updated": 1518440175700
}

```
## Future
### Security
- As of now the APIs are open and unsecure. To begin with we can introduce basic authentication and then introduce JWT based OAuth2 security with spring-security to secure the APIs. 
- We can also introuce authorization apart from authentication to bring resource level access control.
- The web dashboard is open and accessible by all. This can also be controlled by form based login authentication and authorization control.
### Scalability 
We are serving the read/write requests directly from the database. If there are more high reads than there are updates, we can introduce a layer of cache to serve the data.




