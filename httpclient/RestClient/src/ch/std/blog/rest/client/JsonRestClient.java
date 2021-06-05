package ch.std.blog.rest.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import ch.std.blog.rest.json.JsonBodyHandler;

public class JsonRestClient {
	
	public static void main(String[] args) throws IOException, InterruptedException {

		String url = "https://www.simtech-ag.ch/std-ajax/randomfake?min=1&max=1000";
		
		if (args.length > 1) {
			url = args[0];
		}
		// create a client
		var client = HttpClient.newHttpClient();

		// create a request
		var request = HttpRequest.newBuilder(URI.create(url))
				.header("accept", "application/json").build();

		// use the client to send the request
		var response = client.send(request, new JsonBodyHandler<>(HashMap.class));

		Map<?,?> responseMap = response.body().get();
		System.out.println(responseMap);
	}
	
}
