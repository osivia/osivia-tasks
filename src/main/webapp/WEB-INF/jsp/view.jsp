<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<ul class="tasks list-unstyled" data-reload="${reload}" data-reload-url="${reloadUrl}" data-tasks-count="${tasksCount}">
    <c:forEach var="task" items="${tasks.tasks}" varStatus="status">
        <li>
            <!-- Horizontal row -->
            <c:if test="${not status.first}">
                <hr>
            </c:if>
            
            <c:choose>
                <c:when test="${empty task.message}">
                    <div>
                        <small class="text-muted"><fmt:formatDate value="${task.date}" type="date" dateStyle="long" /></small>
                    </div>
                    
                    <c:choose>
                        <c:when test="${task.acknowledgeable and task.closeable}">
                            <!-- Acknowledgeable AND closeable -->
                            <div class="row">
                                <div class="col-xs-6 col-sm-9">
                                    <div>${task.display}</div>
                                </div>
                                
                                <div class="col-xs-6 col-sm-3">                        
                                    <div class="btn-group btn-group-sm pull-right">
                                        <!-- Accept -->
                                        <portlet:actionURL name="accept" var="url">
                                            <portlet:param name="index" value="${status.index}" />
                                        </portlet:actionURL>
                                        
                                        <a href="${url}" class="btn btn-link">
                                            <i class="glyphicons glyphicons-ok"></i>
                                            <span class="sr-only"><op:translate key="ACCEPT" /></span>
                                        </a>
                                        
                                        <!-- Reject -->
                                        <portlet:actionURL name="reject" var="url">
                                            <portlet:param name="index" value="${status.index}" />
                                        </portlet:actionURL>
                                        
                                        <a href="${url}" class="btn btn-link">
                                            <i class="glyphicons glyphicons-remove"></i>
                                            <span class="sr-only"><op:translate key="REJECT" /></span>
                                        </a>
                                        
                                        <!-- Close -->
                                        <portlet:actionURL name="close" var="url">
                                            <portlet:param name="index" value="${status.index}" />
                                        </portlet:actionURL>
                                        
                                        <a href="${url}" class="btn btn-link">
                                            <i class="glyphicons glyphicons-remove"></i>
                                            <span class="sr-only"><op:translate key="CLOSE" /></span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                    
                        <c:when test="${task.acknowledgeable}">
                            <!-- Acknowledgeable -->
                            <div class="row">
                                <div class="col-xs-8 col-sm-10">
                                    <div>${task.display}</div>
                                </div>
                                
                                <div class="col-xs-4 col-sm-2">                        
                                    <div class="btn-group btn-group-sm pull-right">
                                        <!-- Accept -->
                                        <portlet:actionURL name="accept" var="url">
                                            <portlet:param name="index" value="${status.index}" />
                                        </portlet:actionURL>
                                        
                                        <a href="${url}" class="btn btn-link">
                                            <i class="glyphicons glyphicons-ok"></i>
                                            <span class="sr-only"><op:translate key="ACCEPT" /></span>
                                        </a>
                                        
                                        <!-- Reject -->
                                        <portlet:actionURL name="reject" var="url">
                                            <portlet:param name="index" value="${status.index}" />
                                        </portlet:actionURL>
                                        
                                        <a href="${url}" class="btn btn-link">
                                            <i class="glyphicons glyphicons-remove"></i>
                                            <span class="sr-only"><op:translate key="REJECT" /></span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        
                        <c:when test="${task.closeable}">
                            <!-- Closeable -->
                            <div class="row">
                                <div class="col-xs-10 col-sm-11">
                                    <div>${task.display}</div>
                                </div>
                                
                                <div class="col-xs-2 col-sm-1">                        
                                    <div class="btn-group btn-group-sm pull-right">
                                        <!-- Close -->
                                        <portlet:actionURL name="close" var="url">
                                            <portlet:param name="index" value="${status.index}" />
                                        </portlet:actionURL>
                                        
                                        <a href="${url}" class="btn btn-link">
                                            <i class="glyphicons glyphicons-remove"></i>
                                            <span class="sr-only"><op:translate key="CLOSE" /></span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:when>
                        
                        <c:otherwise>
                            <div>${task.display}</div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                
                <c:otherwise>
                    <div><c:out value="${task.message}" escapeXml="false"></c:out></div>
                </c:otherwise>
            </c:choose>
        </li>
    </c:forEach>
    
    <c:if test="${empty tasks.tasks}">
        <li>
            <p class="text-center text-muted"><op:translate key="NO_TASKS" /></p>
        </li>
    </c:if>
</ul>
