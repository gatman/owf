import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.web.context.request.RequestContextHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class UrlMappings {
    static def getAction =
    {
        GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.getRequestAttributes();
        String method = webRequest.getCurrentRequest().getMethod().toUpperCase()
        Map params = webRequest.getParameterMap()

        // parse _method to map to RESTful controller action
        String methodParam = params?."_method"?.toUpperCase()
        if (methodParam) 
        method = methodParam
        switch(method)
        {
            case  'GET' :
            return "list"
                
            case 'POST':
            return "createOrUpdate"
                  
            case 'PUT':
            return "createOrUpdate"
              
            case  'DELETE':
            return "delete"
            
            default:
            return "list"
        }
    }
 
    static def handleDashboardAction =
    {
        GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.getRequestAttributes();
        String method = webRequest.getCurrentRequest().getMethod().toUpperCase()
        Map params = webRequest.getParameterMap()

        if (params?."testCreate" == 'true')
        {
            return "create"
        }

        // parse _method to map to RESTful controller action
        String methodParam = params?."_method"?.toUpperCase()
        if (methodParam == 'PUT' || methodParam == 'DELETE' || methodParam == 'GET' || methodParam == 'POST') {
            method = methodParam
        }
		
        //Perform both Bulk delete and Bulk update...
        //Bulk update...
        if ((params?."viewGuidsToDelete" != null) &&
            (params?."viewsToUpdate" != null) &&
            (method == 'PUT'))
        {
			
            return "bulkDeleteAndUpdate"
        }
		
        //Bulk delete...
        if ((params?."viewGuidsToDelete" != null) && (method == 'DELETE'))
        {
            return "bulkDelete"
			
        }
		
        //Bulk update...
        if ((params?."viewsToUpdate" != null) && (method == 'PUT'))
        {
			
            return "bulkUpdate"
        }

        if (params.isdefault && method == 'GET')
        {
            return "getdefault"
        }
		
        if (params.guid)
        {
            // scan through methods to assign action
            if (method == 'GET') {
                return "show"
            } else if (method == 'POST') {
                return "create"
            } else if (method == 'PUT') {
                return "update"
            } else if (method == 'DELETE') {
                return "delete"
            } else {
                return "show"
            }
        }
		
        return "list"
    }

    static mappings = {
        //"/$controller/$action?/$id?"{
        //    constraints {
        //        // apply constraints here
        //    }
        //}

        "/" {
            controller = "index"
            action = "index"
        }

        "/context/root" {
            controller = "context"
            action = "index"
        }
        // Mapping for generic preference objects
        "/prefs/preference/$namespace?/$path?" {
            controller = "preference"
            action = {
                GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.getRequestAttributes();
                String method = webRequest.getCurrentRequest().getMethod().toUpperCase()
                Map params = webRequest.getParameterMap()

                // parse _method to map to RESTful controller action
                String methodParam = params?."_method"?.toUpperCase()
                if (methodParam == 'PUT' || methodParam == 'DELETE' || methodParam == 'GET' || methodParam == 'POST') {
                    method = methodParam
                }
                //Bulk delete...
                if ((params?."preferencesToDelete" != null) && (method == 'DELETE'))
                {
                    return "bulkDelete"
						
                }
                
                if (params.namespace || params.path) {               
                    // scan through methods to assign action
                    if (method == 'GET') {
                        return (params.path)? "show" : "list"
                    } else if (method == 'POST') {
                        return "create"
                    } else if (method == 'PUT') {
                        return "update"
                    } else if (method == 'DELETE') {
                        return "delete"
                    } else {
                        return (params.path)? "show" : "list"
                    }
                }
                return "list"
            }
        }
		
        "/prefs/hasPreference/$namespace/$path" (controller:"preference", action:"doesPreferenceExist")

        "/prefs/server/resources" (controller:"preference", action:"serverResources")
		
        //Mapping for widget definitions
        "/prefs/widgetDefinition/$widgetGuid?" {
            controller="widgetDefinition"
            action = {
                GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.getRequestAttributes();
                String method = webRequest.getCurrentRequest().getMethod().toUpperCase()
                Map params = webRequest.getParameterMap()
            
                // parse _method to map to RESTful controller action
                String methodParam = params?."_method"?.toUpperCase()
                if (methodParam == 'PUT' || methodParam == 'DELETE' || methodParam == 'GET' || methodParam == 'POST') {
                    method = methodParam
                }
	              
                //Bulk delete...
                if ((params?."widgetGuidsToDelete" != null) && (method == 'DELETE'))
                {
                    return "bulkDelete"
						
                }
	              
                // scan through methods to assign action
                if (method == 'GET') {
                    return (params.widgetGuid)? "show" : "list"
                } else if (method == 'POST') {
                    if (params.limit != null && params.start != null)
                    {
                        return "list"
                    }
                    return "create"
                } else if (method == 'PUT') {
                    return "update"
                } else if (method == 'DELETE') {
                    return "delete"
                } else {
                    return "show"
                }
            }
        }
        // Mapping for person-widget definitions
        "/prefs/widget/$guid?" {
            controller = "personWidgetDefinition"
            action = {
                GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.getRequestAttributes();
                String method = webRequest.getCurrentRequest().getMethod().toUpperCase()
                Map params = webRequest.getParameterMap()

                // parse _method to map to RESTful controller action
                String methodParam = params?."_method"?.toUpperCase()
                if (methodParam == 'PUT' || methodParam == 'DELETE' || methodParam == 'GET' || methodParam == 'POST') {
                    method = methodParam
                }
                
                //Perform both Bulk delete and Bulk update...
                //Bulk update...
                if ((params?."widgetGuidsToDelete" != null) && 
                    (params?."widgetsToUpdate" != null) && 
                    (method == 'PUT'))
                {
                	
                    return "bulkDeleteAndUpdate"
                }
                
                //Bulk delete...
                if ((params?."widgetGuidsToDelete" != null) && (method == 'DELETE'))
                {
                    return "bulkDelete"
                	
                }
				
                //Bulk update...
                if ((params?."widgetsToUpdate" != null) && (method == 'PUT'))
                {
                	
                    return "bulkUpdate"
                }
                
                // scan through methods to assign action
                if (method == 'GET') {
                    return (params.guid)? "show" : "list"
                } else if (method == 'POST') {
                    return "create"
                } else if (method == 'PUT') {
                    return "update"
                } else if (method == 'DELETE') {
                    return "delete"
                } else {
                    return "show"
                }
            }
        }

        "/prefs/widget/listUserAndGroupWidgets" ( controller:'personWidgetDefinition', action:'listUserAndGroupWidgets' )
		
        "/prefs/widgetList" ( controller:'personWidgetDefinition', action:'widgetList' )

        // Mapping for dashboard objects
        "/dashboard/$guid?" {
            controller = "dashboard"
            action = UrlMappings.getAction
        }

        "/prefs/dashboard/$guid?" {
            controller = "dashboard"
            action = UrlMappings.handleDashboardAction
        }
        
        "/prefs/widgetDefinition/dependents" {
            controller = "widgetDefinition"
            action = "dependents"
        }
        
        "/prefs/personWidgetDefinition/dependents" {
            controller = "personWidgetDefinition"
            action = "dependents"
        }

        //old admin urls
        "/prefs/administration/$action?/$id?" {
            controller = "administration"
        }

        //old admin urls
        "/administration/$action?/$id?" {
            controller = "administration"
        }

        // new admin urls
        "/user/$id?"{
            controller = "person"
            action = UrlMappings.getAction
        }
        "/dashboard/restore/$guid?" {
            controller = "dashboard"
            action = 'restore'
        }
        "/group/copyDashboard" {
            controller = "group"
            action = "copyDashboard"
        }
        "/group/$id?" {
            controller = "group"
            action = UrlMappings.getAction
        }
        "/stack/export" {
            controller = "stack"
            action = "export"
        }
        "/stack/import" {
            controller = "stack"
            action = "importStack"
        }
		"/stack/restore/$id?" {
			controller = "stack"
			action = "restore"
		}
        "/stack/$id?" {
            controller = "stack"
            action = UrlMappings.getAction
        }
		
        //Mapping for widget definitions
        "/widget/$widgetGuid?" {
            controller="widget"
            action = UrlMappings.getAction
        }
        "/widget/export" {
            controller = "widget"
            action = "export"
        }
        "/widgetLoadTime" {
            controller="widget"
            action = "saveWidgetLoadTime"
        }
        "/widget/listUserWidgets" {
            controller="personWidgetDefinition"
            action = "listPersonWidgetDefinitions"
        }
        "/widget/approve" {
            controller="personWidgetDefinition"
            action = "approvePersonWidgetDefinitions"
        }
        "/widgettype/list" {
            controller = "widgetType"
            action = "list"
        }

        "/prefs/person/whoami" {
            controller="person"
            action="whoami"
        }

        "/js/config/config.js" {
            controller="config"
            action="config"
        }

        "/testerror"{
            controller='testError'
            action='index'
        }
        "/testerror/throwerror"{
            controller='testError'
            action='throwError'
        }
        "/testerror/throwerror2"{
            controller='testError'
            action='throwError2'
        }
        "/images/$img_name**"{
            controller='theme'
            action='getImageURL'
        }
        "/admin/images/$img_name**"{
            controller='theme'
            action='getImageURL'
            isImageReqAdmin=true
        }
        "/helpFiles"{
            controller='help'
            action='getFiles'
        }
        "/themes"{
            controller='theme'
            action='getAvailableThemes'
        }
        "/themes/$subPath**"{
            controller='mergedDirectoryResource'
            action='get'

            //the file path on the server for where external themes are located
            fileRoot={
                /*
                 * The line below will not work until grails 2.0. At which point
                 * ConfigurationHolder will be deprecated, so these should be 
                 * switched then.  (Also switch in help url)
                 */
                //grailsApplication.config.owf.external.themePath
                ConfigurationHolder.config.owf.external.themePath

            }
            urlRoot="themes"
        }
        "/help/$subPath**"{
            controller='mergedDirectoryResource'
            action='get'

            //the file path on the server for where external help is located
            fileRoot={
                //grailsApplication.config.owf.external.helpPath
                ConfigurationHolder.config.owf.external.helpPath
            }
            urlRoot="help"
        }
        "/js-plugins/$subPath**"{
            controller='mergedDirectoryResource'
            action='get'

            //the file path on the server for where external help is located
            fileRoot={
                //grailsApplication.config.owf.external.jsPluginPath
                ConfigurationHolder.config.owf.external.jsPluginPath
            }
            urlRoot="js-plugins"
        }
        "/metric"{
            controller='metric'
            action={
                GrailsWebRequest webRequest = (GrailsWebRequest) RequestContextHolder.getRequestAttributes();
                String method = webRequest.getCurrentRequest().getMethod().toUpperCase()
                Map params = webRequest.getParameterMap()

                // parse _method to map to RESTful controller action
                String methodParam = params?."_method"?.toUpperCase()
                if (methodParam == 'PUT' || methodParam == 'DELETE' || methodParam == 'GET' || methodParam == 'POST') {
                    method = methodParam
                }
                // scan through methods to assign action
                if (method == 'GET') {
                    return "list"
                } else if (method == 'POST') {
                    return "create"
                }
                //else if (method == 'PUT') {
                //    return "update"
                //}
                //    else if (method == 'DELETE') {
                //        return "bulkDelete"
                //}
                else {
                    return "list"
                }
            }
        }
        "/marketplace/sync/$guid" {
            controller = 'marketplace'
            action = 'retrieveFromMarketplace'
        }
        "/audit" {
            controller='audit'
            action='logMessage'
        }
        "/access" {
            controller='access'
            action='checkAccess'
        }
        "500"(controller: 'error')
    }
}
