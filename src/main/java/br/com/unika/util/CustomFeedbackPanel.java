package br.com.unika.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessagesModel;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class CustomFeedbackPanel extends Panel implements IFeedback {

	private static final long serialVersionUID = 1L;
	private final MessageListView messageListView;
	WebMarkupContainer messagesContainer = new WebMarkupContainer("feedbackul") {
		private static final long serialVersionUID = 1L;

		@Override
		protected void onConfigure() {
			super.onConfigure();
			setVisible(anyMessage());
		}
	};

	public CustomFeedbackPanel(final String id) {
		this(id, null);
	}

	public CustomFeedbackPanel(final String id, IFeedbackMessageFilter filter) {
		super(id);

		add(messagesContainer);

		messageListView = new MessageListView("messages");
		messagesContainer.add(messageListView);

		if (filter != null) {
			setFilter(filter);
		}
	}

	public final boolean anyErrorMessage() {
		return anyMessage(FeedbackMessage.ERROR);
	}

	public final boolean anyMessage() {
		return anyMessage(FeedbackMessage.UNDEFINED);
	}

	public final boolean anyMessage(int level) {
		List<FeedbackMessage> msgs = getCurrentMessages();

		for (FeedbackMessage msg : msgs) {
			if (msg.isLevel(level)) {
				return true;
			}
		}

		return false;
	}
	

	public final FeedbackMessagesModel getFeedbackMessagesModel() {
		return (FeedbackMessagesModel) messageListView.getDefaultModel();
	}

	public final IFeedbackMessageFilter getFilter() {
		return getFeedbackMessagesModel().getFilter();
	}

	public final CustomFeedbackPanel setFilter(IFeedbackMessageFilter filter) {
		getFeedbackMessagesModel().setFilter(filter);
		return this;
	}

	public final Comparator<FeedbackMessage> getSortingComparator() {
		return getFeedbackMessagesModel().getSortingComparator();
	}

	public final CustomFeedbackPanel setSortingComparator(Comparator<FeedbackMessage> sortingComparator) {
		getFeedbackMessagesModel().setSortingComparator(sortingComparator);
		return this;
	}

	@Override
	public boolean isVersioned() {
		return false;
	}

	public final CustomFeedbackPanel setMaxMessages(int maxMessages) {
		messageListView.setViewSize(maxMessages);
		return this;
	}

	protected String getCSSClass(final FeedbackMessage message) {
		String css = "feedback";
		if (message.getLevel() == FeedbackMessage.ERROR || message.getLevel() == FeedbackMessage.FATAL) {
			css = "alert alerta-erro  alert-dismissible fade show shadow-lg hideMe alertStyle";
		}
		if (message.getLevel() == FeedbackMessage.SUCCESS) {
			css = "alert alerta-sucesso    alert-dismissible fade show shadow-lg hideMe alertStyle";
		}

		return css;
	}
	

	protected String getTitulo(final FeedbackMessage message) {
		String titulo = "Sucesso";
		if (message.getLevel() == FeedbackMessage.ERROR || message.getLevel() == FeedbackMessage.FATAL) {
			titulo = "Erro";
		}
		if (message.getLevel() == FeedbackMessage.SUCCESS) {
			titulo = "Informação";
		}

		return titulo;
	}

	protected final List<FeedbackMessage> getCurrentMessages() {
		final List<FeedbackMessage> messages = messageListView.getModelObject();
		return Collections.unmodifiableList(messages);
	}

	protected FeedbackMessagesModel newFeedbackMessagesModel() {
		return new FeedbackMessagesModel(this);
	}

	protected Component newMessageDisplayComponent(String id, FeedbackMessage message) {
		Serializable serializable = message.getMessage();
		Label label = new Label(id, (serializable == null) ? "" : serializable.toString());
		label.setEscapeModelStrings(CustomFeedbackPanel.this.getEscapeModelStrings());
		// label.add(new AttributeModifier("class",getCSSClass(message)));
		return label;
	}

	private final class MessageListView extends ListView<FeedbackMessage> {
		private static final long serialVersionUID = 1L;

		public MessageListView(final String id) {
			super(id);
			setDefaultModel(newFeedbackMessagesModel());
		}

		@Override
		protected IModel<FeedbackMessage> getListItemModel(final IModel<? extends List<FeedbackMessage>> listViewModel,
				final int index) {
			return new AbstractReadOnlyModel<FeedbackMessage>() {
				private static final long serialVersionUID = 1L;

				@Override
				public FeedbackMessage getObject() {
					if (index >= listViewModel.getObject().size()) {
						return null;
					} else {
						return listViewModel.getObject().get(index);
					}
				}
			};
		}

		@Override
		protected void populateItem(final ListItem<FeedbackMessage> listItem) {
			final FeedbackMessage message = listItem.getModelObject();
			message.markRendered();
			final Component label = newMessageDisplayComponent("message", message);
			final AttributeModifier levelModifier = AttributeModifier.replace("class", getCSSClass(message));
			// label.add(levelModifier);
			listItem.add(new Label("titulo", getTitulo(message)));
			listItem.add(levelModifier);
			listItem.add(label);
//			messagesContainer.add(levelModifier);

		}
		

	}
}
