package com.ultra.reactive.Observables;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.ultra.reactive.Utils.O;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

/**
 * <p></p>
 * <p><sub>(24.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class ClickViewObservable<T> implements Observable.OnSubscribe<T>
	{
	private View view;
	private T value;

	private class Listener implements View.OnClickListener
		{
		Subscriber<? super T> subscriber;

		Listener(Subscriber<? super T> _subscriber)
			{
			subscriber=_subscriber;
			}

		@Override
		public void onClick(View v)
			{
			subscriber.onNext(value);
			}
		}
	private class Destructor extends MainThreadSubscription
		{
		@Override
		protected void onUnsubscribe()
			{
			Log.d(O.TAG,"Destructor.onUnsubscribe: ");
			view.setOnClickListener(null);
			}
		}

	public ClickViewObservable(View _view,T _value)
		{
		view=_view;
		value=_value;
		}

	@Override
	public void call(Subscriber<? super T> subscriber)
		{
		verifyMainThread();
		subscriber.add(new Destructor() );
		view.setOnClickListener(new Listener(subscriber) );
		}
	}
