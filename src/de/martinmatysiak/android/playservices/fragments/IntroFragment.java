package de.martinmatysiak.android.playservices.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.plus.PlusClient;

import de.martinmatysiak.android.playservices.PlusClientProvider;
import de.martinmatysiak.android.playservices.R;

public class IntroFragment extends Fragment implements OnClickListener,
		ConnectionCallbacks {

	PlusClient plusClient;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_intro, null);
		view.findViewById(R.id.sign_in_button).setOnClickListener(this);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getActivity() instanceof PlusClientProvider) {
			plusClient = ((PlusClientProvider) getActivity()).getPlusClient();
			plusClient.registerConnectionCallbacks(this);
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button && !plusClient.isConnected()) {
			plusClient.connect();
		}
	}

	@Override
	public void onConnected() {
		// TODO(marmat): Implement.
		((TextView) getView().findViewById(R.id.text_greeting)).setText("Hi, "
				+ plusClient.getAccountName() + "!");
	}

	@Override
	public void onDisconnected() {
		// TODO(marmat): Implement.

	}
}
