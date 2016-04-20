package dcp.tutorial;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class DinnerList_Menus extends TabActivity {
	List<Restaurant> model=new ArrayList<Restaurant>();
	RestaurantAdapter adapter=null;
	EditText name=null;
	EditText address=null;
	EditText notes=null;
	RadioGroup types=null;
	Restaurant current=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		name=(EditText)findViewById(R.id.name);
		address=(EditText)findViewById(R.id.addr);
		notes=(EditText)findViewById(R.id.notes);
		types=(RadioGroup)findViewById(R.id.types);
		
		Button save=(Button)findViewById(R.id.save);
		
		save.setOnClickListener(onSave);
		
		ListView list=(ListView)findViewById(R.id.restaurants);
		
		adapter=new RestaurantAdapter();
		list.setAdapter(adapter);
		
		TabHost.TabSpec spec=getTabHost().newTabSpec("tag1");
		
		spec.setContent(R.id.restaurants);
		spec.setIndicator("List", getResources()
																.getDrawable(R.drawable.list));
		getTabHost().addTab(spec);
		
		spec=getTabHost().newTabSpec("tag2");
		spec.setContent(R.id.details);
		spec.setIndicator("Details", getResources()
																	.getDrawable(R.drawable.restaurant));
		getTabHost().addTab(spec);
		
		getTabHost().setCurrentTab(0);
		
		list.setOnItemClickListener(onListClick);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(getApplication())
																	.inflate(R.menu.option, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId()==R.id.toast) {
			String message = "No restaurant selected";

			if (current != null) {
				message = current.getNotes();
			}
			new AlertDialog.Builder(this)
					.setTitle("Notes")
					.setMessage(message)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.show();

			return(true);
		}
		else if (item.getItemId()==R.id.restaurant){

            Toast.makeText(DinnerList_Menus.this, "Details", Toast.LENGTH_SHORT).show();
            getTabHost().setCurrentTab(1);

            return(true);
		}
		else if (item.getItemId()==R.id.list){

            Toast.makeText(DinnerList_Menus.this, "List", Toast.LENGTH_SHORT).show();
            getTabHost().setCurrentTab(2);

            return(true);
		}
		
		return(super.onOptionsItemSelected(item));
	}
	
	private View.OnClickListener onSave=new View.OnClickListener() {
		public void onClick(View v) {
			current=new Restaurant();
			current.setName(name.getText().toString());
			current.setAddress(address.getText().toString());
			current.setNotes(notes.getText().toString());
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					current.setType("sit_down");
					break;
					
				case R.id.take_out:
					current.setType("take_out");
					break;
					
				case R.id.delivery:
					current.setType("delivery");
					break;
			}
			
			adapter.add(current);
		}
	};
	
	private AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent,
														 View view, int position,
														 long id) {
			current=model.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());
			
			if (current.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			}
			else if (current.getType().equals("take_out")) {
				types.check(R.id.take_out);
			}
			else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
		}
	};
	
	class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		RestaurantAdapter() {
			super(DinnerList_Menus.this,
						android.R.layout.simple_list_item_1,
						model);
		}
		
		public View getView(int position, View convertView,
												ViewGroup parent) {
			View row=convertView;
			RestaurantWrapper wrapper=null;
			
			if (row==null) {													
				LayoutInflater inflater=getLayoutInflater();
				
				row=inflater.inflate(R.layout.row, parent, false);
				wrapper=new RestaurantWrapper(row);
				row.setTag(wrapper);
			}
			else {
				wrapper=(RestaurantWrapper)row.getTag();
			}
			
			wrapper.populateFrom(model.get(position));
			
			return(row);
		}
	}
	
	class RestaurantWrapper {
		private TextView name=null;
		private TextView address=null;
		private ImageView icon=null;
		private View row=null;
		
		RestaurantWrapper(View row) {
			this.row=row;
		}
		
		void populateFrom(Restaurant r) {
			getName().setText(r.getName());
			getAddress().setText(r.getAddress());
	
			if (r.getType().equals("sit_down")) {
				getIcon().setImageResource(R.drawable.ball_red);
			}
			else if (r.getType().equals("take_out")) {
				getIcon().setImageResource(R.drawable.ball_yellow);
			}
			else {
				getIcon().setImageResource(R.drawable.ball_green);
			}
		}
		
		TextView getName() {
			if (name==null) {
				name=(TextView)row.findViewById(R.id.title);
			}
			
			return(name);
		}
		
		TextView getAddress() {
			if (address==null) {
				address=(TextView)row.findViewById(R.id.address);
			}
			
			return(address);
		}
		
		ImageView getIcon() {
			if (icon==null) {
				icon=(ImageView)row.findViewById(R.id.icon);
			}
			
			return(icon);
		}
	}
}
