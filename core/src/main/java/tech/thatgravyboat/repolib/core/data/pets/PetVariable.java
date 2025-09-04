package tech.thatgravyboat.repolib.core.data.pets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.repolib.core.utils.RepoCodec;

public interface PetVariable {

    Codec<PetVariable> CODEC = RepoCodec.STRING.dispatch(
            PetVariable::type, type -> switch (type) {
                case Constant.TYPE -> Constant.CODEC;
                case Conditional.TYPE -> Conditional.CODEC;
                case Linear.TYPE -> Linear.CODEC;
                default -> MapCodec.unit(Constant.ZERO);
            });

    double get(int level);

    String type();

    record Constant(
            double value
    ) implements PetVariable {

        public static final String TYPE = "constant";
        public static final MapCodec<Constant> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
                RepoCodec.DOUBLE.fieldOf("value").forGetter(Constant::value)
        ).apply(it, Constant::new));

        public static final Constant ZERO = new Constant(0);

        @Override
        public double get(int level) {
            return value;
        }

        @Override
        public String type() {
            return TYPE;
        }
    }

    record Conditional(
            int level,
            String operator,
            PetVariable ifTrue,
            PetVariable ifFalse
    ) implements PetVariable {

        public static final String TYPE = "conditional";
        public static final MapCodec<Conditional> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
                RepoCodec.INT.fieldOf("level").forGetter(Conditional::level),
                RepoCodec.STRING.fieldOf("operator").forGetter(Conditional::operator),
                PetVariable.CODEC.fieldOf("if_true").forGetter(Conditional::ifTrue),
                PetVariable.CODEC.fieldOf("if_false").forGetter(Conditional::ifFalse)
        ).apply(it, Conditional::new));

        @Override
        public double get(int level) {
            final boolean condition;
            switch (operator) {
                case ">" -> condition = level > this.level;
                case ">=" -> condition = level >= this.level;
                case "<" -> condition = level < this.level;
                case "<=" -> condition = level <= this.level;
                case "==" -> condition = level == this.level;
                case "!=" -> condition = level != this.level;
                default -> {
                    return 0.0;
                }
            }
            return condition ? ifTrue.get(level) : ifFalse.get(level);
        }

        @Override
        public String type() {
            return TYPE;
        }
    }

    record Linear(
            double base,
            double gain
    ) implements PetVariable {

        public static final String TYPE = "linear";
        public static final MapCodec<Linear> CODEC = RecordCodecBuilder.mapCodec(it -> it.group(
                RepoCodec.DOUBLE.fieldOf("base").forGetter(Linear::base),
                RepoCodec.DOUBLE.fieldOf("gain").forGetter(Linear::gain)
        ).apply(it, Linear::new));

        @Override
        public double get(int level) {
            return base + gain * (level - 1);
        }

        @Override
        public String type() {
            return TYPE;
        }
    }
}
