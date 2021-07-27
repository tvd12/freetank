package org.youngmonkeys.freetank.app.request;

import com.tvd12.ezyfox.binding.annotation.EzyArrayBinding;

import lombok.Data;

@Data
@EzyArrayBinding
public class SyncPositionRequest {
	private String objectType;
	private int objectId;
	private Vec3Data position;
	private Vec3Data rotation;
}
