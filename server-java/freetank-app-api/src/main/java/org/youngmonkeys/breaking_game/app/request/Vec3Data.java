package org.youngmonkeys.breaking_game.app.request;

import com.tvd12.ezyfox.binding.annotation.EzyArrayBinding;
import lombok.Data;

@Data
@EzyArrayBinding
public class Vec3Data {
    private double x;
    private double y;
    private double z;
}
