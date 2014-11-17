<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page language="Java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
    <title>Files</title>

    <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="http://getbootstrap.com/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="http://getbootstrap.com/dist/css/bootstrap-responsive.min.css" rel="stylesheet">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251" >
</head>
<body>
<div class="container">
    <h1>Files on server</h1>
    <form:form method="post" action="files/add" commandName="file" enctype="multipart/form-data">


        <tr><td>File to upload: </td><td><form:input type="file" path="file" /></td></tr>
        <br/>
        <button type="submit" class="btn btn-default">Upload File</button>
    </form:form>

    <c:if test="${!empty files}">
        <h3>Uploaded Files</h3>
        <table class="table table-bordered table-striped">
            <thead>
            <tr>
                <th>File</th>
                <th>Path</th>
                <th>Size</th>
                <th>&nbsp;</th>
                <th>&nbsp;</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach items="${files}" var="file">

                <tr>
                    <td>${file.fileName}</td>
                    <td>${file.filePath}</td>
                    <td>${file.fileSize}</td>
                    <td>
                        <form:form action="files/delete/${file.fileId}" method="post"><input type="submit"
                                                                                   class="btn btn-danger btn-mini"
                                                                                   value="Delete"/>
                        </form:form>
                    </td>
                    <td>
                        <form:form action="files/download/${file.fileId}" method="post"><input type="submit"
                                                                                       class="btn btn-danger btn-mini"
                                                                                       value="Download"/>
                        </form:form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>
<br>

</body>
</html>
