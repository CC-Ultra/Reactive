package com.ultra.reactive.Observables;

import android.util.Log;
import android.view.MotionEvent;
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

public class TouchViewObservable implements Observable.OnSubscribe<String>
	{
	private View view;

	private class Listener implements View.OnTouchListener
		{
		Subscriber<? super String> subscriber;

		Listener(Subscriber<? super String> _subscriber)
			{
			subscriber=_subscriber;
			}

		@Override
		public boolean onTouch(View v,MotionEvent event)
			{
			switch(event.getAction() )
				{
				case MotionEvent.ACTION_DOWN:
					subscriber.onNext("down");
					break;
				case MotionEvent.ACTION_UP:
					subscriber.onNext("up");
					break;
				case MotionEvent.ACTION_MOVE:
					if( ( (int)event.getX() )%10 == 0 && ( (int)event.getY() )%10 == 0)
						subscriber.onNext("x= "+ event.getX() +"y="+ event.getY() );
					break;
				}
			return true;
			}
		}
	private class Destructor extends MainThreadSubscription
		{
		@Override
		protected void onUnsubscribe()
			{
			Log.d(O.TAG,"Destructor.onUnsubscribe: ");
			view.setOnTouchListener(null);
			}
		}

	public TouchViewObservable(View _view)
		{
		view=_view;
		}

	@Override
	public void call(Subscriber<? super String> subscriber)
		{
		verifyMainThread();
		subscriber.add(new Destructor() );
		view.setOnTouchListener(new Listener(subscriber) );
		}
	}
