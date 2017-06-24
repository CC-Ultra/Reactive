package com.ultra.reactive.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.ultra.reactive.Observables.ClickViewObservable;
import com.ultra.reactive.Observables.TouchViewObservable;
import com.ultra.reactive.R;
import com.ultra.reactive.Utils.O;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity
	{
	private CompositeSubscription allSusubscriptions= new CompositeSubscription();

	private class UnsubscribeListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			allSusubscriptions.unsubscribe();
			}
		}
	private class XSubscriber<T> extends Subscriber<T>
		{
		@Override
		public void onCompleted()
			{
			Log.d(O.TAG,"onCompleted: ");
			}
		@Override
		public void onError(Throwable e)
			{
			Log.e(O.TAG,"onError",e);
			}

		@Override
		public void onNext(T value)
			{
			Log.d(O.TAG,""+ value);
			}
		}

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		Button btn1= (Button)findViewById(R.id.btn1);
		LinearLayout field= (LinearLayout)findViewById(R.id.field);

		Observable<Float> clicks= Observable.create(new ClickViewObservable<>(field,3.14F));
		Observable<String> touches= Observable.create(new TouchViewObservable(field) );
		allSusubscriptions.add(clicks.subscribe(new XSubscriber<>() ) );
		allSusubscriptions.add(touches.subscribe(new XSubscriber<>() ) );
		btn1.setOnClickListener(new UnsubscribeListener() );
		}
	}
