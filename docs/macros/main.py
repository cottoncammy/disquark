import textwrap


def define_env(env):

    @env.macro
    def snippet(file, tag):
        root = env.conf['docs_dir']
        if 'snippet_dir' in env.variables:
           root = env.variables['snippet_dir']

        f = open(root + "/" + file)

        inRecordingMode = False
        c = ""
        for line in f:
            if not inRecordingMode:
                if "<" + tag + ">" in line:
                    inRecordingMode = True
            elif "</" + tag + ">" in line:
                inRecordingMode = False
            else:
                c += line

        f.close()
        if not c:
            raise Exception(f"Unable to find tag '{tag}' in '{file}'")

        c = textwrap.dedent(c)
        return c