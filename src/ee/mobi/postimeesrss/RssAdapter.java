package ee.mobi.postimeesrss;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RssAdapter extends BaseAdapter {
	
	private Context context;
	private List<ListItem> items;
	
	public RssAdapter(Context context, List<ListItem> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int pos) {
		return items.get(pos);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, android.R.layout.simple_list_item_2, null);
		}
		TextView primary = (TextView)convertView.findViewById(android.R.id.text1);
		TextView secondary = (TextView)convertView.findViewById(android.R.id.text2);
		
		ListItem listItem = (ListItem)getItem(position);
		
		primary.setText(listItem.title);
		secondary.setText(listItem.pubDate);
		
		return convertView;
	}
}
