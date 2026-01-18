package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;

import java.util.List;
import java.util.Objects;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

public class SchuldPersistenceMapper {
    public static SchuldBo toSchuldBo(SchuldEntity source) {
        if (source == null) {
            return null;
        }
        return new SchuldBo(
                source.getId(),
                source.getJahr(),
                convert(source.getArt(), SchuldArtBo.class),
                source.getGlaeubiger(),
                source.getBetrag(),
                source.getZinsen());
    }

    public static SchuldEntity toSchuldEntity(SchuldBo source) {
        return new SchuldEntity(
                source.getId(),
                source.getJahr(),
                convert(source.getArt(), SchuldArtEntity.class),
                source.getGlaeubiger(),
                source.getBetrag(),
                source.getZinsen());
    }

    public static List<SchuldBo> toSchuldBoList(List<SchuldEntity> entityList) {
        return entityList.stream().map(SchuldPersistenceMapper::toSchuldBo).filter(Objects::nonNull).toList();
    }
}
