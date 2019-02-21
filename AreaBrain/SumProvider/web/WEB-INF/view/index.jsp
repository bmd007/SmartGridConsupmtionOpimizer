<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index</title>
</head>
<body>
    JSP simple indexxxxx:

    <c:forEach items="${users}" var="u">
        <c:out value="${u.userId}"/>:<c:out value="${u.email}"/><br>
    </c:forEach>
    <br><br>

    <div>
        <sec:authorize access="hasRole('USER')">
            <label><a href="#">Edit this page</a> | This part is visible only to USER</label>
        </sec:authorize>
    </div>
</body>
</html>
