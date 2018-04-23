<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:url value="/" var="raiz" />
<c:url value="/assets" var="assets" />
<c:url value="/app" var="urlOcorrencias" />
<c:url value="/app/ocorrencia" var="urlVisualizarOcorrencia" />
<c:url value="/app/ocorrencia/nova" var="urlNovaOcorrencia" />
<c:url value="/app/ocorrencia/assumir" var="urlAssumirOcorrencia" />
<c:url value="/app/ocorrencia/encerrar" var="urlEncerrarOcorrencia" />



<!DOCTYPE html>
<html>
<head>
	<c:import url="../templates/head.jsp"/>
	<style>
	#tabelaOcorrencias img{
		background-image: linear-gradient(to left bottom, #2432cc, #cc29cc, #cc2525);
		width: 100px;
	}
	</style>
</head>
<body>
	<c:import url="../templates/header.jsp"/>
	<main class="container">
		<a href="${urlNovaOcorrencia}" class="btn btn-red d-block ma-l-auto ma-t-s" style="max-width: 220px"> Abrir ocorr�ncia</a>
		<h1 class="fx-slide-in">Ocorr�ncias</h1>
		<section id="sectionOcorrencias">
			<h2>Classificar por: </h2>
			<%--Filtros de busca --%>
			<form action="${urlOcorrencias}" method="get" class="flex-grid ma-b-l" style="max-width: 400px;">
				<div class="row">
				<div class="col flex-2">
					<%-- <select name="pesquisa">
						<c:forEach items="${pesquisas}" var="pesquisa">
							<option value="${pesquisa}">${pesquisa.descricao}</option>
						</c:forEach>
					</select> --%>
					<%-- Quando o form:input n�o est� em um form:form, devemos informar o name manualmente --%>
					<form:select path="tiposBusca" items="${tiposBusca}" name="pesquisa"/>
				</div>
				<div class="col flex-1">
					<button class="btn btn-blue" type="submit">Pesquisar</button>
				</div>
				</div>
			</form>
			
			<%-- Tabela de ocorr�ncias --%>
			<table id="tabelaOcorrencias" class="table container read-container">
				<thead>
					<tr>
						<th>#</th>
						<th>Ocorr�ncias</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ocorrencias}" var="ocorrencia">
						<tr>
							<%--- Sinalizador de status --%>
							<td></td>
							<%-- Descri��o da ocorr�ncia --%>
							<td>
								<p class="ocorrencia-id">
									<a href="${urlVisualizarOcorrencia}/editar?id=${ocorrencia.id}">
										${ocorrencia.id}
									</a>
								</p>
								<h4><c:out value="${ocorrencia.titulo}" escapeXml="true"/></h4>
								<p class="ocorrencia-detalhe"><b class="color-pink">Data de abertura: </b>
								<fmt:formatDate value="${ocorrencia.dataCadastro}" pattern="dd/MM/yyyy kk:mm:ss"/>
								
								</p>
								<p class="ocorrencia-detalhe"><b class="color-pink">�ltima modifica��o: </b>
									<fmt:formatDate value="${ocorrencia.dataModificacao}"  pattern="dd/MM/yyyy kk:mm:ss"/>
								</p>
								<p class="ocorrencia-detalhe"><b class="color-pink">Data de conclus�o: </b>
									<fmt:formatDate value="${ocorrencia.dataConclusao}"  pattern="dd/MM/yyyy kk:mm:ss"/>
								</p>
							</td>
							<%--Quem atendeu ocorrencia/link de atendimento--%>
							<td>
								<%-- Links de a��o - Assumir e encerrar ocorr�ncia --%>
								<c:choose>
									<c:when test="${empty ocorrencia.tecnico}">
										<a href="${urlAssumirOcorrencia}?id=${ocorrencia.id}">Assumir</a>
									</c:when>
									<%-- 
									1� - Deve ter sido atendido
									2� - A ocorr�ncia n�o deve ter sido conclu�da
									3� - O emissor da ocorr�ncia deve ser o usu�rio logado
									 --%>
									<c:when
									 test="${not empty ocorrencia.tecnico
									 and empty ocorrencia.dataConclusao
									 and usuarioAutenticado.id eq ocorrencia.emissor.id}">
									 	<a href="${urlEncerrarOcorrencia}?id=${ocorrencia.id}">Encerrar ocorr�ncia</a>
									 </c:when>
								</c:choose>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</section>
	</main>
	<c:import url="../templates/botoesFlutuantes.jsp"/>
</body>
</html>