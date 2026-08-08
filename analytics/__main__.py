import os
import runpy

for file in os.listdir(os.path.dirname(__file__)):
    if file.endswith(".py") and file != "__main__.py":
        runpy.run_path(os.path.join(os.path.dirname(__file__), file))
