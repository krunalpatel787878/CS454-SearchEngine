<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CS454 - Search Engine</title>
</head>
<body>
	<template id="resultstemplate"> 
	<c:forEach items="${AutoList}" var="each">
		<option value="${each}">${each}</option>
	</c:forEach> </template>
	<form action="SearchServlet" method="post">
		<input type="text" name="search" id="search" list="searchresults"
			autocomplete="off" />
		<datalist id="searchresults"></datalist>
		<input type="submit" value="Search">
	</form>
	<table border="1px solid">
		<tr>
			<th>Score</th>
			<th>Link</th>
		</tr>
		<c:forEach items="${result}" var="row">
			<tr>
				<td>${row.score}</td>
				<td><a href="${row.link}" target="_blank">${row.link}</a></td>
			</tr>
		</c:forEach>
	</table>
	<script>
		var search = document.querySelector('#search');
		var results = document.querySelector('#searchresults');
		var templateContent = document.querySelector('#resultstemplate').content;
		search.addEventListener('keyup', function handler(event) {
			while (results.children.length)
				results.removeChild(results.firstChild);
			var inputVal = new RegExp(search.value.trim(), 'i');
			var clonedOptions = templateContent.cloneNode(true);
			var set = Array.prototype.reduce.call(clonedOptions.children,
					function searchFilter(frag, el) {
						if (inputVal.test(el.textContent)
								&& frag.children.length < 5)
							frag.appendChild(el);
						return frag;
					}, document.createDocumentFragment());
			results.appendChild(set);
		});
	</script>

</body>
</html>