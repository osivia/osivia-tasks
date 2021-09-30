<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<c:set var="namespace"><portlet:namespace /></c:set>


<div class="tasks" data-tasks-count="${tasksCount}">

    <c:if test="${not empty discussionUrl}">
        <div class="clearfix mb-3">
            <a href="${discussionUrl}" class="badge badge-secondary float-right"  >
                <span><op:translate key="TASK_ACCESS_TO_DISCUSSIONS" /></span>
            </a>
        </div>
    </c:if>   
     
    <!-- Help -->
    <c:if test="${not empty tasks.help}">
        <div class="clearfix">${tasks.help}</div>
        <hr>
    </c:if>

    <!-- Notfications -->
    <ul class="list-unstyled">
        <c:forEach var="task" items="${tasks.tasks}" varStatus="status">
            <li>
                <!-- Horizontal row -->
                <c:if test="${not status.first}">
                    <hr>
                </c:if>
                
                <c:choose>
                    <c:when test="${empty task.message}">
                        <div class="clearfix position-relative">
                            <div class="d-flex flex-align-center">
                                <c:if test="${not empty task.initiator.avatar.url}">
                                    <div class="pr-3">
                                        <img src="${task.initiator.avatar.url}" alt="" class="avatar">
                                    </div>
                                </c:if>
                                
                                <div>
                                    <!-- Display -->
                                    <div>${task.display}</div>
                                    
                                    <!-- Date -->
                                    <div>
                                        <small class="text-muted"><fmt:formatDate value="${task.date}" type="date" dateStyle="long" /></small>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Actions -->
                            <div class="pull-right">
                                <c:if test="${task.acknowledgeable}">
                                    <!-- Accept -->
                                    <portlet:actionURL name="accept" var="url">
                                        <portlet:param name="index" value="${status.index}" />
                                    </portlet:actionURL>
                                    
                                    <a href="${url}" class="btn btn-link btn-sm" data-ajax-shadowbox="#shadowbox-${namespace}-${status.index}">
                                        <span><op:translate key="TASK_ACCEPT" /></span>
                                    </a>
                                    
                                    <!-- Reject -->
                                    <portlet:actionURL name="reject" var="url">
                                        <portlet:param name="index" value="${status.index}" />
                                    </portlet:actionURL>
                                    
                                    <a href="${url}" class="btn btn-link btn-sm" data-ajax-shadowbox="#shadowbox-${namespace}-${status.index}">
                                        <span><op:translate key="TASK_REJECT" /></span>
                                    </a>
                                </c:if>
                                
                                <c:if test="${task.closeable}">
                                    <!-- Close -->
                                    <portlet:actionURL name="close" var="url">
                                        <portlet:param name="index" value="${status.index}" />
                                    </portlet:actionURL>
                                    
                                    <a href="${url}" class="btn btn-link btn-sm" data-ajax-shadowbox="#shadowbox-${namespace}-${status.index}">
                                        <span><op:translate key="TASK_CLOSE" /></span>
                                    </a>
                                </c:if>
                            </div>
                            
                            <div id="shadowbox-${namespace}-${status.index}" class="ajax-shadowbox">
                                <div class="d-flex flex-grow-1 flex-align-center justify-content-center"> 
                                <div class="spinner-border">
                                    <span class="sr-only"><op:translate key="AJAX_REFRESH" /></span>
                                </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    
                    <c:otherwise>
                        <p><c:out value="${task.message}" escapeXml="false"></c:out></p>
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
     

</div>
