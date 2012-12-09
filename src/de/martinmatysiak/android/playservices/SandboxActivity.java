package de.martinmatysiak.android.playservices;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import de.martinmatysiak.android.playservices.fragments.IntroFragment;
import de.martinmatysiak.android.playservices.fragments.MapsFragment;
import de.martinmatysiak.android.playservices.fragments.PlusFragment;

public class SandboxActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, PlusClientProvider {

	private static final int PLUS_CLIENT_REQUEST = 1;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter sectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager viewPager;

	/**
	 * The Google+ API client.
	 */
	PlusClient plusClient;

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private Fragment[] fragments = { new IntroFragment(),
				new PlusFragment(), new MapsFragment() };

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			return fragments[i];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "INTRO";
			case 1:
				return "PLUS";
			case 2:
				return "MAPS";
			}
			return null;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sandbox);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);

		plusClient = new PlusClient(this, this, this, Scopes.PLUS_PROFILE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sandbox, menu);
		return true;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d("SandboxActivity", "onConnectionFailed");
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, PLUS_CLIENT_REQUEST);
			} catch (SendIntentException e) {
				plusClient.connect();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == PLUS_CLIENT_REQUEST && responseCode == RESULT_OK) {
			plusClient.connect();
		}
	}

	@Override
	public void onConnected() {
		Log.d("SandboxActivity", "onConnected");
	}

	@Override
	public void onDisconnected() {
		Log.d("SandboxActivity", "onDisconnected");
	}

	@Override
	public PlusClient getPlusClient() {
		// This is used to allow Fragments access to the client. Is there a
		// better way of doing this?
		return plusClient;
	}
}
