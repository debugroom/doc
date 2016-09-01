<%@page pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>掲示板</title>
</head>
<body>
<h1>掲示板</h1>
<s:link href="1-all">全部</s:link>
<s:link href="1-${f:h(pageSize)}">1-</s:link>
<s:link href="${f:h(countRes[0] - 10)}-${f:h(countRes[0]) }">最新10</s:link>
<html:errors/>
<table>
	<c:forEach var="r" varStatus="s" items="${reses}">
	<tr>
		<td>${f:h(r.id) }</td>
		<td>名前: ${f:h(r.name) }</td>
		<td>投稿日: ${f:h(r.date) }</td>
	</tr>
	<tr>
		<td>${f:h(r.entry) }</td>
	</tr>
	</c:forEach>
</table>

<s:form>
<table>
	<tr>
		<td>名前:<html:text property="name" value="名無しさん" /></td>
	</tr>
	<tr>
		<td><html:textarea property="entry" /></td>
	</tr>
</table>
<input type="submit" name="insert" value="CREATE" />
</s:form>

<c:if test="${(start - pageSize) > 0}">
<s:link href="${f:h(start - pageSize)}-${f:h(start - 1) }">前の${f:h(pageSize) }件</s:link>
</c:if>
<c:if test="${start != 1 && start < 10}">
<s:link href="1-${pageSize}">前の${f:h(pageSize) }件</s:link>
</c:if>
<c:if test="${(start + pageSize) < countRes[0]}">
<s:link href="${f:h(start + pageSize)}-${f:h((start + pageSize) + (pageSize - 1))}">次の${f:h(pageSize) }件</s:link>
</c:if>
</body>
</html>