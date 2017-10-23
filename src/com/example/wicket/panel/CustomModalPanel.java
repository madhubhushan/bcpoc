package com.example.wicket.panel;


import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.CoreLibrariesContributor;

public class CustomModalPanel extends ModalWindow
{
	private static final long serialVersionUID = 1L;

	private static final ResourceReference JAVASCRIPT = new JavaScriptResourceReference(
			CustomModalPanel.class, "CustomModalPanel.js");

		private static final ResourceReference CSS = new CssResourceReference(CustomModalPanel.class,
			"CustomModalPanel.css");
    
    
	public CustomModalPanel(String id) {
		super(id);
	}
	
	public CustomModalPanel(final String id, final IModel<?> model) {
        super(id, model);
    }
	
	@Override
	public void renderHead(final IHeaderResponse response)
	{
		//super.renderHead(response);

		CoreLibrariesContributor.contributeAjax(getApplication(), response);
		
		response.render(JavaScriptHeaderItem.forReference(JAVASCRIPT));

		response.render(CssHeaderItem.forReference(CSS));
	}
}
