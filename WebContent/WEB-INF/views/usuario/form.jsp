<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:url value="/" var="raiz" />
<c:url value="/assets" var="assets" />
<c:url value="/app/adm/usuario/deletar" var="urlUsuarioDeletar" />
<c:url value="/app/adm/usuario/salvar" var="urlSalvarUsuario" />


<!DOCTYPE html>
<html>
<head>
	<c:import url="../templates/head.jsp"/>
</head>
<body>
	<c:import url="../templates/header.jsp"/>
	<main class="container read-container">
		<h1>Cadastro de Usuário</h1>
		<form:form modelAttribute="usuario" action="${urlSalvarUsuario}" method="post" class="labels-d-block" enctype="multipart/form-data">
			<form:hidden path="id"/>
			<div class="flex-grid">
				<div class="row">
					<div class="col flex-1">
						<label for="inputNome">NOME</label>
						<form:input path="nome"/>
						<%-- <input name="nome" type="text" id="inputNome"/> --%>
						<%--<form:errors path="nome" element="div" cssClass="error"/> --%>
						<form:errors path="nome" cssClass="error"/>
					</div>
					<div class="col flex-2">
						<label for="inputSobrenome">SOBRENOME</label>
						<form:input path="sobrenome"/>
						<%-- <input name="sobrenome" type="text" id="inputSobrenome"/> --%>
						<form:errors path="sobrenome" cssClass="error"/>
					</div>
				</div>
				<c:if test="${empty usuario.id}">
					<div class="row">
						<div class="col flex-1">
							<label for="inputEmail">EMAIL</label>
							<form:input path="email"/>
							<%-- <input name="email" type="email" id="inputEmail"/> --%>
							<form:errors path="email" cssClass="error"/>
						</div>
					</div>
					<div class="row">
						<div class="col flex-1">
							<label for="inputSenha">SENHA</label>
							<form:password path="senha"/>
							<%-- <input name="senha" type="password" id="inputSenha"/> --%>
							<form:errors path="senha" cssClass="error"/>
						</div>
						<div class="col flex-1">
							<label for="inputConfirmaSenha">CONFIRMAR SENHA</label>
							<input type="password" id="inputConfirmaSenha" name="confirmacaoSenha"/>
						</div>
					</div>
				</c:if>
				<div class="row">
					<div class="col flex-1">
						<label for="inputFoto">Foto de Perfil</label>
						<input type="file" name="foto"><%-- accept=".png, .jpg, .jpeg" --%>
					</div>
				</div>
				<div class="row">
					<div class="col">
						<label>
							Administrador
							<%-- <input type="checkbox" name="isAdministrador" id="inputAdministrador" class="d-inline" checked="checked"> --%>
							<form:checkbox path="administrador"/>
						</label>
					</div>
				</div>
			</div>
			<button type="submit" class="btn btn-blue">SALVAR</button>
			<c:if test="${not empty usuario.id}">
				<a href="${urlUsuarioDeletar}?id=${usuario.id}" class="btn btn-red">DELETAR</a>
			</c:if>
		</form:form>
	</main>
	<c:import url="../templates/botoesFlutuantes.jsp" />
</body>
</html>