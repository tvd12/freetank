package org.youngmonkeys.freetank.app.request;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.Data;

@Data
@EzyObjectBinding
public class SyncDataRequest {
	private String to;
	private String command;
	private Object data;
}
