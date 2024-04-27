# Book Shop -service

### Automation of Book Shop  operation systems

This system includes:

* Reformed a User details
* Chance to perform actions on Book
* Chance to perform actions on card
* Using Spring security to authenticate users
* the ability to save orders to the Cart
* granting and restricting permissions and roles to Users by super admin
* Get user statistics of all orders books
* View all orders

```java

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableWebSecurity
public class BookShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookShopApplication.class, args);
    }

```

***You can get the services listed above in this table through the links***

| No |                                                                               Services                                                                               | Status |
|:--:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:------:|
| 1  |                     [User Service](https://github.com/KuronboevAsadbek/Book-Shop/tree/main/src/main/java/uz/bookshop/controller/user_controller)                     |   ✅    |
| 2  |                    [Admin Service](https://github.com/KuronboevAsadbek/Book-Shop/tree/main/src/main/java/uz/bookshop/controller/admin_controller)                    |   ✅    |
| 3  |              [Super Admin Service](https://github.com/KuronboevAsadbek/Book-Shop/tree/main/src/main/java/uz/bookshop/controller/super_admin_controller)              |   ✅    |
| 4  |         [Book Service](https://github.com/KuronboevAsadbek/Book-Shop/blob/main/src/main/java/uz/bookshop/controller/service_controller/BookController.java)          |   ✅    |
| 5  |      [Comment Service](https://github.com/KuronboevAsadbek/Book-Shop/blob/main/src/main/java/uz/bookshop/controller/service_controller/CommentController.java)       |   ✅    |
| 6  |         [Cart Service ](https://github.com/KuronboevAsadbek/Book-Shop/blob/main/src/main/java/uz/bookshop/controller/service_controller/CartController.java)         |   ✅    |
| 7  | [Order Details Service](https://github.com/KuronboevAsadbek/Book-Shop/blob/main/src/main/java/uz/bookshop/controller/service_controller/OrderDetailsController.java) |   ✅    |
| 8  |        [Order Service](https://github.com/KuronboevAsadbek/Book-Shop/blob/main/src/main/java/uz/bookshop/controller/service_controller/OrderController.java)         |   ✅    |
| 9  |   [Statistics Service](https://github.com/KuronboevAsadbek/Book-Shop/blob/main/src/main/java/uz/bookshop/controller/service_controller/StatisticsController.java)    |   ✅    |
