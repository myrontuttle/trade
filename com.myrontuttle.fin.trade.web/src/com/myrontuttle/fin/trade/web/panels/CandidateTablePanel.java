package com.myrontuttle.fin.trade.web.panels;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.web.data.SortableCandidateDataProvider;
import com.myrontuttle.fin.trade.web.pages.CandidatePage;
import com.myrontuttle.fin.trade.web.service.AdaptAccess;
import com.myrontuttle.fin.trade.web.service.EvolveAccess;
import com.myrontuttle.fin.trade.web.service.PortfolioAccess;
import com.myrontuttle.fin.trade.web.service.WatchlistAccess;

public class CandidateTablePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	
	private static final DecimalFormat df = new DecimalFormat("#,##0.00"); 

	@SuppressWarnings({ "rawtypes" })
	public CandidateTablePanel(String id, long groupId) {
		super(id);

		List<IColumn<Candidate, String>> columns = new ArrayList<IColumn<Candidate, String>>();

		columns.add(new PropertyColumn<Candidate, String>(new Model<String>("ID"), "candidateId", "candidateId"));
		columns.add(new PropertyColumn(new Model<String>("Born In"), "bornInGen"));
		columns.add(new PropertyColumn(new Model<String>("Last Expressed"), "lastExpressedGen"));
		columns.add(new AbstractColumn<Candidate, String>(new Model<String>("Watch Symbols")) {
			@Override
			public void populateItem(Item<ICellPopulator<Candidate>> cellItem,
					String componentId, IModel<Candidate> model) {
				try {
					Candidate candidate = model.getObject();
					if (candidate != null && candidate.getWatchlistId() != null) {
						String[] symbols = WatchlistAccess.getWatchlistService().
								retrieveHoldings(candidate.getCandidateId(), candidate.getWatchlistId());
						cellItem.add(new Label(componentId, Arrays.toString(symbols)));
					} else {
						cellItem.add(new Label(componentId, ""));
					}
				} catch (Exception e) {
					cellItem.add(new Label(componentId, e.getMessage()));
				}
			}
		});

		columns.add(new AbstractColumn<Candidate, String>(new Model<String>("Details")) {
			public void populateItem(Item<ICellPopulator<Candidate>> cellItem, String componentId,
				IModel<Candidate> model) {
				cellItem.add(new DetailsLinkPanel(componentId, model));
			}
		});
		
		columns.add(new AbstractColumn<Candidate, String>(new Model<String>("Available Cash")) {
			@Override
			public void populateItem(Item<ICellPopulator<Candidate>> cellItem,
					String componentId, IModel<Candidate> model) {
				try {
					Candidate candidate = model.getObject();
					if (candidate != null && candidate.getPortfolioId() != null) {
						double value = PortfolioAccess.getPortfolioService().
								getAvailableBalance(candidate.getCandidateId(), candidate.getPortfolioId());
						cellItem.add(new Label(componentId, "$" + df.format(value)));
					} else {
						cellItem.add(new Label(componentId, ""));
					}
				} catch (Exception e) {
					cellItem.add(new Label(componentId, e.getMessage()));
				}
			}
		});
		
		columns.add(new AbstractColumn<Candidate, String>(new Model<String>("Unrealized Gain")) {
			@Override
			public void populateItem(Item<ICellPopulator<Candidate>> cellItem,
					String componentId, IModel<Candidate> model) {
				try {
					Candidate candidate = model.getObject();
					
					if (candidate != null && candidate.getPortfolioId() != null) {
						double value = PortfolioAccess.getPortfolioService().
							analyze(candidate.getCandidateId(), candidate.getPortfolioId(), 
									"Unrealized Gain (Absolute)");
						cellItem.add(new Label(componentId, "$" + df.format(value)));
					} else {
						cellItem.add(new Label(componentId, ""));
					}
				} catch (Exception e) {
					cellItem.add(new Label(componentId, e.getMessage()));
				}
			}
		});

		columns.add(new AbstractColumn<Candidate, String>(new Model<String>("Download")) {
			@Override
			public void populateItem(Item<ICellPopulator<Candidate>> cellItem, String componentId,
				IModel<Candidate> model) {
				cellItem.add(new DownloadCandidatePanel(componentId, model));
			}
		});
		
		columns.add(new AbstractColumn<Candidate, String>(new Model<String>("Delete")) {
			@Override
			public void populateItem(Item<ICellPopulator<Candidate>> cellItem, String componentId,
				IModel<Candidate> model) {
				cellItem.add(new DeleteCandidatePanel(componentId, model));
			}
		});
		
		//columns.add(new PropertyColumn(new Model<String>("Genome"), "genomeString"));

		DataTable dataTable = new DefaultDataTable<Candidate, String>("candidates", columns,
				new SortableCandidateDataProvider(groupId), 20);

		add(dataTable);
	}

	class DetailsLinkPanel extends Panel {
		/**
		 * @param id component id
		 * @param model model for candidate
		 */
		public DetailsLinkPanel(String id, IModel<Candidate> model) {
			super(id, model);
			add(new Link("details") {
				@Override
				public void onClick() {
					long candidateId = ((Candidate)getParent().getDefaultModelObject()).getCandidateId();
					CandidatePage cp = new CandidatePage(candidateId);
					setResponsePage(cp);
				}
			});
		}
	}

	class DownloadCandidatePanel extends Panel {
		/**
		 * @param id component id
		 * @param model model for candidate
		 */
		public DownloadCandidatePanel(String id, IModel<Candidate> model) {
			super(id, model);
			add(new Link<Void>("download") {
			    @Override
			    public void onClick() {
			        AbstractResourceStreamWriter rstream = new AbstractResourceStreamWriter() {
			            @Override
			            public void write(OutputStream output) throws IOException {
			            	ByteArrayOutputStream bos = new ByteArrayOutputStream();
			            	ObjectOutput out = new ObjectOutputStream(bos);
			            	out.writeObject((Candidate)getParent().getDefaultModelObject());
			                output.write(bos.toByteArray());
			                
			                try {
			                	if (out != null) {
			                		out.close();
			                	}
			                } catch (IOException ex) {
			                	// ignore close exception
			                }
			                try {
			                	bos.close();
			                } catch (IOException ex) {
			                	// ignore close exception
			                }
			            }
			        };
			        ResourceStreamRequestHandler handler = 
			        		new ResourceStreamRequestHandler(rstream, "candidate.ser");        
			        getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
			    }
			});
		}
	}
	
	class DeleteCandidatePanel extends Panel {
		/**
		 * @param id component id
		 * @param model model for contact
		 */
		public DeleteCandidatePanel(String id, IModel<Candidate> model) {
			super(id, model);

			final Form<Candidate> form = new Form<Candidate>("deleteCandidateForm", model);
			form.add(new Button("delete") {
				public void onSubmit() {
					Candidate candidate = ((Candidate)getParent().getDefaultModelObject());
					EvolveAccess.getEvolveService().deleteCandidateExpression(
							candidate.getGroupId(), candidate.getGenome());
					AdaptAccess.getDAO().removeCandidate(candidate.getCandidateId());
				}
			});
			add(form);
		}
	}

}
