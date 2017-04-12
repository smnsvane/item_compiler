package sql;

import helper.DamageType;
import helper.WeaponType;

public class Weapon {
    public final WeaponType weaponType;
    public final DamageType damageType;
    public final int range;
    public final int minDamage;
    public final int maxDamage;
    
    public Weapon(WeaponType weaponType, DamageType damageType, int range, int minDamage, int maxDamage) {
        this.weaponType = weaponType;
        this.damageType = damageType;
        this.range = range;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }
}
