package com.itlao.repairservice;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.itlao.repairservice.utils.SendRequestUtil;
import com.itlao.utils.http.handler.RequestStringHandler;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityFeekback extends Activity {

	private Button bt_pinfen_ok;
	private RequestStringHandler readtaskListHandler;
	private String str_id;
	private EditText et_feedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_marks_feedback);
		initView();

		Intent intent = getIntent();
		str_id = intent.getStringExtra("id");

		setHandler(this);
		bt_pinfen_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!et_feedback.getText().toString().equals("")) {

					sendResquest();

				} else {

					Toast.makeText(ActivityFeekback.this, "反馈内容不能为空",
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	private void initView() {

		bt_pinfen_ok = (Button) findViewById(R.id.bt_pinfen_ok);
		et_feedback = (EditText) findViewById(R.id.et_feedback);

	}

	public void sendResquest() {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("id", Long.parseLong(str_id));
		params.put("feedback", et_feedback.getText().toString());
		params.put("score", 6);

		try {

			SendRequestUtil.post("AndroidCtrl.do?action=userFeedback", params,
					readtaskListHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHandler(Context context) {
		readtaskListHandler = new RequestStringHandler(context) {

			private boolean isfeedback = false;

			@Override
			public void handleString(String response) throws Exception {
				JSONObject json = new JSONObject(response);

				isfeedback = json.getBoolean("isfeedback");

				if (isfeedback) {

					Toast.makeText(ActivityFeekback.this, "反馈成功",
							Toast.LENGTH_SHORT).show();
					finish();

				} else {

					Toast.makeText(ActivityFeekback.this, "反馈失败",
							Toast.LENGTH_SHORT).show();
				}

			}

		};
	}

}
