<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TODO Application</title>
    <script>
        // Redirect to index.html
        window.location.href = '<%= request.getContextPath() %>/index.html';
    </script>
</head>
<body>
    Redirecting to TODO application...
</body>
</html>