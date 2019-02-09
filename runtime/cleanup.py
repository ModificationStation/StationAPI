# -*- coding: utf-8 -*-
"""
Created on Sat Apr  9 13:51:48 2011

@author: ProfMobius & Searge
@version: v1.0
"""

import sys, shutil, os, glob, logging, time, stat
from optparse import OptionParser
from commands import Commands
if sys.version_info[0] == 3:
    raw_input=input
def main(conffile=None, force=False):

    if sys.version_info[0] == 3:
        print ('ERROR : Python3 is not supported yet.')
        sys.exit(1)

    if not force:
        print('WARNING:')
        print('The cleanup script will delete all folders created by MCP, including the')
        print('src folder which may contain changes you made to the code.')
        answer = raw_input('If you really want to clean up, enter "Yes" ')
        if answer.lower() not in ['yes']:
            print('You have not entered "Yes", aborting the clean up process')
            sys.exit(0)

    commands = Commands(conffile)
    commands.checkupdates()

    commands.logger.info ('> Cleaning temp')
    if not reallyrmtree(commands.dirtemp):
        commands.logger.error ('failed cleaning temp')

    commands.logger.info ('> Cleaning src')
    if not reallyrmtree(commands.dirsrc):
        commands.logger.error ('failed cleaning src')

    commands.logger.info ('> Cleaning bin')
    if not reallyrmtree(commands.dirbin):
        commands.logger.error ('failed cleaning bin')

    commands.logger.info ('> Cleaning reobf')
    if not reallyrmtree(commands.dirreobf):
        commands.logger.error ('failed cleaning reobf')

    commands.logger.info ('> Cleaning jars')
    reallyrmtree(os.path.join(commands.dirjars, 'saves'))
    reallyrmtree(os.path.join(commands.dirjars, 'texturepacks'))
    reallyrmtree(os.path.join(commands.dirjars, 'world'))
    if os.path.exists(os.path.join(commands.dirjars, 'server.log')):
        os.remove(os.path.join(commands.dirjars, 'server.log'))
    for txt_file in glob.glob(os.path.join(commands.dirjars, '*.txt')):
        os.remove(txt_file)

    commands.logger.info ('> Cleaning logs')
    logging.shutdown()
    reallyrmtree(commands.dirlogs)


def reallyrmtree(dir):
    i = 0
    try:
        while os.stat(dir) and i < 20:
            shutil.rmtree(dir, onerror=onerror)
            i += 1
    except OSError:
        pass

    try:
        os.stat(dir)
    except OSError:
        return True
    else:
        return False

def onerror(func, path, exc_info):
    if not os.access(path, os.W_OK):
        os.chmod(path, stat.S_IWUSR)

    time.sleep(0.5)

    try:
        func(path)
    except OSError:
        pass


if __name__ == '__main__':
    parser = OptionParser(version='MCP %s' % Commands.MCPVersion)
    parser.add_option('-f', '--force', action='store_true', dest='force', help='force cleanup', default=False)
    parser.add_option('-c', '--config', dest='config', help='additional configuration file')
    (options, args) = parser.parse_args()
    main(options.config, options.force)
