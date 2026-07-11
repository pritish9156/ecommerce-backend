package util;

import org.json.JSONObject;
import com.razorpay.*;

public class RazorpayUtil {

	private static final String KEY_ID = "rzp_test_T2maD6MifbeAuB";

	private static final String KEY_SECRET = "uDZo4aOX2l390EBdwSXir6ks";

	public static Order createOrder(int amount) throws Exception {

		RazorpayClient client = new RazorpayClient(KEY_ID, KEY_SECRET);

		JSONObject options = new JSONObject();

		options.put("amount", amount * 100);

		options.put("currency", "INR");

		options.put("receipt", "receipt_" + System.currentTimeMillis());

		return client.orders.create(options);
	}
}