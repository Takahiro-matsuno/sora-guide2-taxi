<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table>
        <tr>
        <th>会社番号</th>
        <th>社名</th>
        <th>連絡先</th>
        <th>所在地</th>
        </tr>
        <%
            for(int i = 0,i < ${fn:length(result)-1 , i++){
        %>
        <tr>
            <td>${result[i].company_id}</td>
            <td>${result[i].name}</td>
            <td>${result[i].contact}</td>
            <td>${result[i].location}</td>
        </tr>
        <%
            }
        %>
        </table>
    </body>
</html>