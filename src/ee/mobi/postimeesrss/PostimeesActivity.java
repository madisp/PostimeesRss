package ee.mobi.postimeesrss;

import java.util.List;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import us.monoid.web.Resty;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class PostimeesActivity extends ListActivity {

	private RssLoadTask loader = new RssLoadTask();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminate(true);

		setContentView(R.layout.main);

		loader.execute("http://www.postimees.ee/rss/");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ListItem item = (ListItem) l.getAdapter().getItem(position);
		startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(item.url)));
	}

	private class RssLoadTask extends AsyncTask<String, Void, List<ListItem>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected List<ListItem> doInBackground(String... params) {
			List<ListItem> ret = new Vector<ListItem>();

			Resty resty = new Resty();
			for (String url : params) {
				try {
					XPath xpath = XPathFactory.newInstance().newXPath();

					NodeList items = resty.xml(url).get("/rss/channel/item");

					for (int i = 0; i < items.getLength(); i++) {

						ListItem listItem = new ListItem();
						Node item = items.item(i);

						listItem.title = xpath.evaluate("title/text()", item);
						listItem.url = xpath.evaluate("link/text()", item);
						listItem.pubDate = xpath.evaluate("pubDate/text()", item);

						ret.add(listItem);

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return ret;
		}

		@Override
		protected void onPostExecute(List<ListItem> result) {
			super.onPostExecute(result);
			setProgressBarIndeterminateVisibility(false);
			setListAdapter(new RssAdapter(PostimeesActivity.this, result));
		}
	}
}