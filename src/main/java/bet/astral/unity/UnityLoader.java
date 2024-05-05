package bet.astral.unity;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class UnityLoader implements PluginLoader {
	@Override
	public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {

		MavenLibraryResolver resolver = new MavenLibraryResolver();
		// Maven Central
		resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());

		// Cloud
		String cloudFramework = "2.0.0-beta.4";
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-core:"+cloudFramework), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-paper:"+cloudFramework), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-brigadier:"+cloudFramework), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-minecraft-extras:"+cloudFramework), null));
		resolver.addDependency(new Dependency(new DefaultArtifact("org.incendo:cloud-services:"+cloudFramework), null));
		pluginClasspathBuilder.addLibrary(resolver);


		// Classgraph for reflections
		resolver.addDependency(new Dependency(new DefaultArtifact("io.github.classgraph:classgraph:4.8.165"), null));

		// Database stuff (mysql)
		resolver.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:5.1.0"), null));
		pluginClasspathBuilder.addLibrary(resolver);
	}
}

