<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<ul class="list-unstyled">
    <c:forEach var="task" items="${tasks.tasks}" varStatus="status">
        <li>
            <!-- Horizontal row -->
            <c:if test="${not status.first}">
                <hr>
            </c:if>
            
            <p class="text-muted">
                <small><fmt:formatDate value="${task.date}" type="date" dateStyle="long" /></small>
            </p>
            
            <div class="row">
                <div class="col-xs-8 col-sm-10">
                    <div>${task.display}</div>
                </div>
                
                <div class="col-xs-4 col-sm-2">
                    <c:if test="${task.acknowledgeable}">
                        <div class="btn-group btn-group-sm pull-right">
                            <!-- Accept -->
                            <portlet:actionURL name="accept" var="url">
                                <portlet:param name="path" value="${task.document.path}" />
                            </portlet:actionURL>
                            <a href="${url}" class="btn btn-default">
                                <i class="glyphicons glyphicons-ok"></i>
                                <span class="sr-only"><op:translate key="ACCEPT" /></span>
                            </a>
                            
                            <!-- Reject -->
                            <portlet:actionURL name="reject" var="url">
                                <portlet:param name="path" value="${task.document.path}" />
                            </portlet:actionURL>
                            <a href="${url}" class="btn btn-default">
                                <i class="glyphicons glyphicons-remove"></i>
                                <span class="sr-only"><op:translate key="REJECT" /></span>
                            </a>
                        </div>
                    </c:if>
                </div>
            </div>
        </li>
    </c:forEach>
    
    <c:if test="${empty tasks.tasks}">
        <li>
            <p class="text-center text-muted"><op:translate key="NO_TASKS" /></p>
        </li>
    </c:if>
</ul>
