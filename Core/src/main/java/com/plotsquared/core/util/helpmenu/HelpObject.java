/*
 * PlotSquared, a land and world management plugin for Minecraft.
 * Copyright (C) IntellectualSites <https://intellectualsites.com>
 * Copyright (C) IntellectualSites team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.plotsquared.core.util.helpmenu;

import com.plotsquared.core.command.Argument;
import com.plotsquared.core.command.Command;
import com.plotsquared.core.configuration.caption.Templates;
import com.plotsquared.core.configuration.caption.TranslatableCaption;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.util.StringMan;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

public class HelpObject {

    static final MiniMessage MINI_MESSAGE = MiniMessage.builder().build();

    private final String rendered;

    public HelpObject(final Command command, final String label, final PlotPlayer<?> audience) {
        rendered = MINI_MESSAGE.serialize(MINI_MESSAGE.parse(
                TranslatableCaption.of("help.help_item").getComponent(audience),
                Template.of("usage", command.getUsage().replace("{label}", label)),
                Template.of("alias", command.getAliases().isEmpty() ? "" : StringMan.join(command.getAliases(), " | ")),
                Templates.of(audience, "desc", command.getDescription()),
                Template.of("arguments", buildArgumentList(command.getRequiredArguments())),
                Template.of("label", label)
        ));
    }

    @Override
    public String toString() {
        return rendered;
    }

    private String buildArgumentList(final Argument<?>[] arguments) {
        if (arguments == null) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for (final Argument<?> argument : arguments) {
            builder.append("[").append(argument.getName()).append(" (")
                    .append(argument.getExample()).append(")],");
        }
        return arguments.length > 0 ? builder.substring(0, builder.length() - 1) : "";
    }

}
