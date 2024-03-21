<#if form.hasErrors()>
<div class="error">
        <#list form.errors as error>
            <p>${msg(error)}</p>
        </#list>
    </div>
</#if>