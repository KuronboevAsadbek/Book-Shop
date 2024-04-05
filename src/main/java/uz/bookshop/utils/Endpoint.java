package uz.bookshop.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Endpoint {

    public static final String AUTH = "/auth";
    public static final String ADMIN = "/admin";
    public static final String SUPER_ADMIN = "/superadmin";
    public static final String COMMENT = "/comment";
    public static final String STATISTICS = "/statistics";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH_TOKEN = "/refreshToken";
    public static final String UPDATE = "/update";
    public static final String ADD_ROLE = "/addRole";
    public static final String UPDATE_USER = "/updateUser";
    public static final String BOOK = "/book";
    public static final String ACTIVE_USERS = "/activeUsers";
    public static final String CREATE = "/create";
    public static final String DELETE = "/delete/{id}";
    public static final String DELETE_CART = "/delete";
    public static final String GET_ONE = "/getOne/{id}";
    public static final String GET_ALL = "/getall";
    public static final String GET_USER_BOOKS = "/getUserBooks";
    public static final String ADD = "/add";
    public static final String CART = "/cart";
    public static final String ADD_MINUS_OR_PLUS = "/addMinusOrPlus";
    public static final String OPEN = "/open";
    public static final String ORDER = "/order";
    public static final String ORDER_DETAILS = "/orderDetails";
    public static final String DELETE_ONE_CART = "/deleteOneCart/{id}";
    public static final String REGISTERED_USERS = "/lastWeekRegisteredUsers";
    public static final String GET_USERS_BY_BEGIN_DATE_AND_END_DATE = "/getbydate";
    public static final String POPULAR_BOOKS = "/popularBooks";
}
